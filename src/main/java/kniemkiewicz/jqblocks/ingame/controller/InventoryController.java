package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.Inventory;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.GeometryUtils;
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
public class InventoryController implements InputListener, EventListener {

  private static int DROP_RANGE = 4 * Sizes.BLOCK;

  @Autowired
  Inventory inventory;
  @Autowired
  SpringBeanProvider provider;
  @Autowired
  SolidBlocks solidBlocks;
  @Autowired
  PlayerController playerController;
  @Autowired
  InputContainer inputContainer;

  public static Log logger = LogFactory.getLog(InventoryController.class);

  public void listen(Input input, int delta) {
    int k = KeyboardUtils.getPressedNumericKey(input);
    if (k == 0) {
      inventory.setSelectedIndex(9);
    } else if (k > 0 && k <= inventory.getSize()) {
      inventory.setSelectedIndex(k - 1);
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
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
    if (clazz == null) return false;
    ItemController controller = provider.getBean(clazz, true);
    MovingPhysicalObject dropObject = controller.getDropObject(inventory.getSelectedItem(), x, y);
    if (dropObject == null) return false;
    dropObject(dropObject);
    inventory.removeSelectedItem();
    return true;
  }

  @Override
  public void listen(List<Event> events) {
    if (events.size() == 0) return;
    for (Event e : events) {
      if (e instanceof MouseClickEvent) {
        MouseClickEvent mce = (MouseClickEvent)e;
        if (!mce.isProcessed() && ((mce.getButton() == Button.RIGHT)
            && !KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput()))) {
          if (dropItem(mce.getLevelX(), mce.getLevelY())) {
            mce.markProcessed(true);
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
