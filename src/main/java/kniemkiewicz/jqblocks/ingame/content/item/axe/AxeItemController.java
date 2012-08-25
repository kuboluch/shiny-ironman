package kniemkiewicz.jqblocks.ingame.content.item.axe;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.resource.Wood;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.CompletionEffect;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.ResourceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class AxeItemController extends AbstractActionItemController<AxeItem> {

  @Autowired
  private Backgrounds backgrounds;

  @Autowired
  private ResourceInventoryController resourceInventoryController;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  private MovingObjects movingObjects;

  @Autowired
  private CollisionController collisionController;

  private CompletionEffect completionEffect;

  Wood wood = new Wood();

  @Override
  protected boolean canPerformAction(int x, int y) {
    BackgroundElement backgroundElement = getBackgroundElement(new Rectangle(x, y, 1, 1));
    if (backgroundElement != null && backgroundElement.isResource()) {
      if (((ResourceBackgroundElement) backgroundElement).getMiningItemType().isAssignableFrom(AxeItem.class)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    Rectangle rect = null;
    BackgroundElement backgroundElement = getBackgroundElement(new Rectangle(x, y, 1, 1));
    if (backgroundElement != null && backgroundElement.isResource()) {
      if (((ResourceBackgroundElement) backgroundElement).getMiningItemType().isAssignableFrom(AxeItem.class)) {
        assert backgroundElement.getShape() instanceof Rectangle;
        rect = (Rectangle) backgroundElement.getShape();
      }
    }
    assert rect != null;
    return rect;
  }

  @Override
  protected void startAction(AxeItem item) {
    assert completionEffect == null;
    completionEffect = new CompletionEffect(affectedRectangle);
    renderQueue.add(completionEffect);
  }

  @Override
  protected void stopAction(AxeItem item) {
    assert completionEffect != null;
    renderQueue.remove(completionEffect);
    completionEffect = null;
    if (wood.getAmount() == 0) return;
    transformToPile(wood);
    //playerResourceController.addResource(wood);
    wood = new Wood();
  }

  @Override
  protected void updateAction(AxeItem item, int delta) {
    BackgroundElement be = getAffectedBackgroundElement();
    if (be != null && be.isResource()) {
      ResourceBackgroundElement<Wood> rbe = ((ResourceBackgroundElement) be);
      if (rbe.getMiningItemType().isAssignableFrom(AxeItem.class)) {
        Wood mined = rbe.mine(delta);
        mined.transferTo(wood);
        transformToPile(wood);
      }
    }
  }

  private void transformToPile(Wood wood) {
    if (wood.getAmount() > 0) {
      float rectMaxX = affectedRectangle.getX() + affectedRectangle.getWidth();
      float rectMaxY = affectedRectangle.getY() + affectedRectangle.getHeight();
      int pileX = Sizes.roundToBlockSizeX(affectedRectangle.getX());
      int pileY = Sizes.roundToBlockSizeY(rectMaxY) - ResourceObject.SIZE;
      while (wood.getAmount() > 0 && pileX < rectMaxX) {
        // CollisionController will find object with x + width = pileX so we add 1. Analogically for y coordinate.
        ResourceObject<Wood> woodPile = getWoodPile(pileX + 1, pileY + 1);
        if (woodPile == null) {
          woodPile = createAndDropWoodPile(pileX, pileY);
        }
        wood = woodPile.addResource(wood);
        completionEffect.setPercentage((woodPile.getResourceAmount() * 100) / woodPile.getResourceMaxPileSize());
        pileX += ResourceObject.SIZE;
      }
      if (wood.getAmount() > 0) {
        // Only if there is no more room for piles
        wood.deplete();
      }
    }
  }

  private ResourceObject<Wood> createAndDropWoodPile(int x, int y) {
    ResourceObject<Wood> woodObject = new ResourceObject<Wood>(new Wood(), x, y);
    resourceInventoryController.dropObject(woodObject);
    return woodObject;
  }

  private ResourceObject<Wood> getWoodPile(int x, int y) {
    ResourceObject<Wood> woodObject = null;
    List<ResourceObject> resourceObjects =
        Collections3.collect(collisionController.fullSearch(MovingObjects.PICKABLE, new Rectangle(x, y, 1, 1)), ResourceObject.class);
    for (ResourceObject ro : resourceObjects) {
      if (ro.isSameType(new Wood())) {
        woodObject = (ResourceObject<Wood>) ro;
      }
    }
    return woodObject;
  }

  @Override
  protected boolean isActionCompleted() {
    return false;
  }

  @Override
  protected void onAction() {
  }

  private BackgroundElement getAffectedBackgroundElement() {
    return getBackgroundElement(affectedRectangle);
  }

  private BackgroundElement getBackgroundElement(Rectangle rect) {
    BackgroundElement backgroundElement = null;
    Iterator<BackgroundElement> it = backgrounds.intersects(rect);
    if (it.hasNext()) {
      backgroundElement = it.next();
    }
    assert !it.hasNext();
    return backgroundElement;
  }

  @Override
  public DroppableObject getObject(AxeItem item, int centerX, int centerY) {
    return null;
  }
}
