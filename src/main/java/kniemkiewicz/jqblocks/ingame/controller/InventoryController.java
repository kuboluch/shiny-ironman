package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.item.Inventory;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
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
  @Autowired
  Inventory inventory;
  @Autowired
  SpringBeanProvider provider;

  public static Log logger = LogFactory.getLog(InventoryController.class);

  public void listen(Input input, int delta) {
    int k = KeyboardUtils.getPressedNumericKey(input);
    if (k == 0) {
      inventory.setSelectedIndex(9);
    } else if (k > 0) {
      inventory.setSelectedIndex(k - 1);
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }

  private boolean dropItem(int x, int y) {
    logger.info("Dropping");
    return true;
  }

  @Override
  public void listen(List<Event> events) {
    if (events.size() == 0) return;
    for (Event e : events) {
      if (e instanceof MouseClickEvent) {
        MouseClickEvent mce = (MouseClickEvent)e;
        if (!mce.isProcessed() && (mce.getButton() == Button.RIGHT)) {
          if (dropItem(mce.getLevelX(), mce.getLevelY())) {
            return;
          }
        }
      }
    }
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getController();
    if (clazz != null) {
      ItemController controller = provider.getBean(clazz, true);
      controller.listen(inventory.getSelectedItem(), events);
    }
  }
}
