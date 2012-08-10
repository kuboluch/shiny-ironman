package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.PlayerResourceController;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.AxeItem;
import kniemkiewicz.jqblocks.ingame.object.CompletionEffect;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.ResourceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.resource.ResourceObject;
import kniemkiewicz.jqblocks.ingame.resource.Wood;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.util.Collections3;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class AxeItemController extends AbstractActionItemController<AxeItem> {
  public static Log logger = LogFactory.getLog(AxeItemController.class);

  @Autowired
  private Backgrounds backgrounds;

  @Autowired
  private ResourceInventoryController resourceInventoryController;

  @Autowired
  private PlayerResourceController playerResourceController;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  private MovingObjects movingObjects;

  private CompletionEffect completionEffect;

  Wood wood = new Wood();

  @Override
  boolean canPerformAction(int x, int y) {
    BackgroundElement backgroundElement = getBackgroundElement(new Rectangle(x, y, 1, 1));
    if (backgroundElement != null && backgroundElement.isResource()) {
      if (((ResourceBackgroundElement) backgroundElement).getMiningItemType().isAssignableFrom(AxeItem.class)) {
        return true;
      }
    }
    return false;
  }

  @Override
  Rectangle getAffectedRectangle(int x, int y) {
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
  void startAction(AxeItem item) {
    assert completionEffect == null;
    completionEffect = new CompletionEffect(affectedRectangle);
    renderQueue.add(completionEffect);
  }

  @Override
  void stopAction(AxeItem item) {
    assert completionEffect != null;
    renderQueue.remove(completionEffect);
    completionEffect = null;
    if (wood.getAmount() == 0) return;
    transformToPile(wood);
    //playerResourceController.addResource(wood);
    wood = new Wood();
  }

  @Override
  void updateAction(AxeItem item, int delta) {
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
      int pileX = Sizes.roundToBlockSizeX(affectedRectangle.getX());
      int pileY = Sizes.roundToBlockSizeY(affectedRectangle.getMaxY()) - ResourceObject.SIZE;
      for (int i = 0; wood.getAmount() > 0 && pileX < affectedRectangle.getMaxX(); i++, pileX += ResourceObject.SIZE + 1) {
        ResourceObject<Wood> woodPile = getWoodPile(pileX, pileY);
        if (woodPile == null) {
          woodPile = createAndDropWoodPile(pileX, pileY);
        }
        wood = woodPile.addResource(wood);
        completionEffect.setPercentage((woodPile.getResourceAmount() * 100) / woodPile.getResourceMaxPileSize());
      }
      if (wood.getAmount() > 0) {
        // Only if there is no more room for piles
        wood.deplete();
      }
    }
  }

  private ResourceObject<Wood> createAndDropWoodPile(int x, int y) {
    ResourceObject<Wood> woodObject = new ResourceObject<Wood>(new Wood(), x, y);
    woodObject.addTo(renderQueue, movingObjects);
    resourceInventoryController.dropObject(woodObject);
    return woodObject;
  }

  private ResourceObject<Wood> getWoodPile(int x, int y) {
    ResourceObject<Wood> woodObject = null;
    List<ResourceObject> resourceObjects =
        Collections3.collect(movingObjects.intersects(new Rectangle(x ,y, 1, 1)), ResourceObject.class);
    for (ResourceObject ro : resourceObjects) {
      if (ro.isSameType(new Wood())) {
        woodObject = (ResourceObject<Wood>) ro;
      }
    }
    return woodObject;
  }

  @Override
  boolean isActionCompleted() {
    return false;
  }

  @Override
  void onAction() {
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
  public Shape getDropObjectShape(AxeItem item, int centerX, int centerY) {
    return null;
  }

  @Override
  public MovingPhysicalObject getDropObject(AxeItem item, int centerX, int centerY) {
    return null;
  }
}
