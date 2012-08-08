package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceObject;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.IterableIterator;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
@Component
public class ResourceInventoryController implements InputListener, EventListener {

  private static int DROP_RANGE = 4 * Sizes.BLOCK;

  @Autowired
  ResourceInventory inventory;
  @Autowired
  SpringBeanProvider provider;
  @Autowired
  SolidBlocks solidBlocks;
  @Autowired
  private MovingObjects movingObjects;
  @Autowired
  PlayerController playerController;
  @Autowired
  InputContainer inputContainer;

  public static Log logger = LogFactory.getLog(ResourceInventoryController.class);

  public void listen(Input input, int delta) {
    int f = KeyboardUtils.getPressedFunctionKey(input);
    if (f > 0 && f <= inventory.getSize()) {
      inventory.setSelectedIndex(f - 1);
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }

  public void dropObject(MovingPhysicalObject dropObject) {
    Shape shape = dropObject.getShape();
    // We will use this infinite vertical rectangle too look where item should drop, as
    // there is no applicable free fall implementation yet.
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    rect.setHeight(Sizes.MAX_Y - Sizes.MIN_Y);
    rect.setWidth(rect.getWidth() - 1);
    int minY = Sizes.MAX_Y;
    for (AbstractBlock block : solidBlocks.intersects(rect)) {
      if (block.getShape().getY() < minY) {
        minY = (int) block.getShape().getY();
      }
    }
    dropObject.setY((int) (minY - shape.getHeight() - 1));
  }

  private boolean dropItem(int x, int y) {
    if (!AbstractActionItemController.isInRange(x, y, playerController.getPlayer(), DROP_RANGE)) return false;
    if (conflictingObjectExists(x, y)) return false;
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
    if (clazz == null) return false;
    ItemController controller = provider.getBean(clazz, true);
    Shape dropObjectShape = controller.getDropObjectShape(inventory.getSelectedItem(), x, y);
    if (dropObjectShape == null) return false;
    if (!isOnSolidGround(dropObjectShape)) return false;
    MovingPhysicalObject dropObject = controller.getDropObject(inventory.getSelectedItem(), x, y);
    dropObject(dropObject);
    inventory.removeSelectedItem();
    return true;
  }

  private boolean conflictingObjectExists(int x, int y) {
    Rectangle rect = new Rectangle(x, y, 1, 1);
    List<ResourceObject> resourceObjects =
        Collections3.collect(movingObjects.intersects(rect), ResourceObject.class);
    if (!resourceObjects.isEmpty()) return true;
    IterableIterator<AbstractBlock> iter = solidBlocks.intersects(rect);
    if (iter.hasNext()) return true;
    return false;
  }

  private boolean isOnSolidGround(Shape shape) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    rect.setY(rect.getMaxY() + 1);
    rect.setWidth(rect.getWidth() - 1);
    rect.setHeight(1);
    int blockCount = 0;
    IterableIterator<AbstractBlock> iter = solidBlocks.intersects(rect);
    while (iter.hasNext()) {
      blockCount++;
      iter.next();
    }
    if (blockCount != (rect.getWidth() + 1) / Sizes.BLOCK) {
      return false;
    }
    return true;
  }

  @Override
  public void listen(List<Event> events) {
    if (events.size() == 0) return;
    for (Event e : events) {
      if (e instanceof MouseClickEvent) {
        MouseClickEvent mce = (MouseClickEvent) e;
        if (!mce.isProcessed() && (mce.getButton() == Button.RIGHT)
            && KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput())) {
          if (dropItem(mce.getLevelX(), mce.getLevelY())) {
            return;
          }
        }
      }
    }
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
    if (clazz != null) {
      ItemController controller = provider.getBean(clazz, true);
      controller.listen(inventory.getSelectedItem(), events);
    }
  }
}
