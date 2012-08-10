package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
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
  PlayerController playerController;
  @Autowired
  InputContainer inputContainer;
  @Autowired
  CollisionController collisionController;

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
    dropObject.setY((int)(solidBlocks.getBlocks().getUnscaledDropHeight(shape) - shape.getHeight() - 1));
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
        Collections3.collect(collisionController.fullSearch(MovingObjects.OBJECT_TYPES, rect), ResourceObject.class);
    if (!resourceObjects.isEmpty()) return true;
    return solidBlocks.getBlocks().collidesWithNonEmpty(rect);
  }

  private boolean isOnSolidGround(Shape shape) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    RawEnumTable<WallBlockType> table = solidBlocks.getBlocks();
    int y = table.toYIndex((int) rect.getMaxY() + 1);
    int x1 = table.toXIndex((int) rect.getX());
    int x2 = table.toXIndex((int) rect.getMaxX() - 1);
    for (int x = x1; x < x2; x++) {
      if (table.getValueForUnscaledPoint(x, y) == WallBlockType.EMPTY) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void listen(List<Event> events) {
    if (events.size() == 0) return;
    for (Event e : events) {
      if (e instanceof MousePressedEvent) {
        MousePressedEvent mpe = (MousePressedEvent) e;
        if (!mpe.isConsumed() && (mpe.getButton() == Button.RIGHT)
            && KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput())) {
          if (dropItem(mpe.getLevelX(), mpe.getLevelY())) {
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
