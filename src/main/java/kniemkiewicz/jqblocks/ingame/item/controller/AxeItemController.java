package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.controller.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.AxeItem;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.ResourceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.resource.Resource;
import kniemkiewicz.jqblocks.ingame.object.resource.ResourceObject;
import kniemkiewicz.jqblocks.ingame.object.resource.Wood;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class AxeItemController extends AbstractActionItemController<AxeItem> {
  public static Log logger = LogFactory.getLog(AxeItemController.class);

  @Autowired
  private Backgrounds backgrounds;

  @Autowired
  private InventoryController inventoryController;

  @Autowired
  private Player player;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  private MovingObjects movingObjects;

  Resource wood = new Wood();

  @Override
  boolean canPerformAction(Rectangle rect) {
    BackgroundElement backgroundElement = getBackgroundElement(rect);
    if (backgroundElement != null && backgroundElement.isResource()) {
      if (((ResourceBackgroundElement) backgroundElement).getMiningItemType().isAssignableFrom(AxeItem.class)) {
        return true;
      }
    }
    return false;
  }

  @Override
  void startAction(AxeItem item) {  }

  @Override
  void stopAction(AxeItem item) {
    if (wood.getAmount() == 0) return;
    ResourceObject ob = new ResourceObject(wood, (int)player.getXMovement().getPos(), (int)player.getYMovement().getPos());
    ob.addTo(renderQueue, movingObjects);
    // There is a high chance that this object will be immediately picked up.
    inventoryController.dropObject(ob);
    wood = new Wood();
  }

  @Override
  void updateAction(AxeItem item, int delta) {
    BackgroundElement backgroundElement = getAffectedBackgroundElement();
    if (backgroundElement != null && backgroundElement.isResource()) {
      Resource resource = ((ResourceBackgroundElement) backgroundElement).mine(delta);
      wood.add(resource);
    }
  }

  @Override
  boolean isActionCompleted() {
    return false;
  }

  @Override
  void onAction() {  }

  private BackgroundElement getAffectedBackgroundElement() {
    return getBackgroundElement(affectedBlock);
  }

  private BackgroundElement getBackgroundElement(Rectangle rect) {
    BackgroundElement backgroundElement = null;
    Iterator<AbstractBackgroundElement> it = backgrounds.intersects(rect);
    if (it.hasNext()) {
      backgroundElement = it.next();
    }
    assert !it.hasNext();
    return backgroundElement;
  }

  @Override
  public MovingPhysicalObject getDropObject(AxeItem item, int centerX, int centerY) {
    return null;
  }
}
