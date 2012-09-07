package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyReleasedEvent;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.util.Collections3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UIController implements EventListener {

  @Autowired
  MainGameUI mainGameUI;

  boolean buildKeyBlock = false;
  boolean constructKeyBlock = false;
  boolean inventoryKeyBlock = false;

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) KeyPressedEvent.class, (Class) KeyReleasedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    List<KeyPressedEvent> keyPressedEvents = Collections3.collect(events, KeyPressedEvent.class);
    if (!keyPressedEvents.isEmpty()) {
      for (KeyPressedEvent e : keyPressedEvents) {
        handleKeyPressedEvent(e);
      }
    }

    List<KeyReleasedEvent> keyReleasedEvents = Collections3.collect(events, KeyReleasedEvent.class);
    if (!keyReleasedEvents.isEmpty()) {
      for (KeyReleasedEvent e : keyReleasedEvents) {
        handleKeyReleasedEvent(e);
      }
    }
  }

  private void handleKeyPressedEvent(KeyPressedEvent e) {
    if (KeyboardUtils.isBuildMenuKey(e.getKey())) {
      if (!buildKeyBlock) {
        if (mainGameUI.isWorkplaceWidgetVisible()) {
          mainGameUI.hideWorkplaceWidget();
        } else {
          mainGameUI.showWorkplaceWidget();
        }
        buildKeyBlock = true;
      }
    }

    if (KeyboardUtils.isProduceMenuKey(e.getKey())) {
      if (!constructKeyBlock) {
        if (mainGameUI.isConstructWidgetVisible()) {
          mainGameUI.hideConstructWidget();
        } else {
          mainGameUI.showConstructWidget();
        }
        constructKeyBlock = true;
      }
    }

    if (KeyboardUtils.isInventoryKey(e.getKey())) {
      if (!inventoryKeyBlock) {
        if (mainGameUI.isInventoryWidgetVisible()) {
          mainGameUI.hideInventoryWidget();
        } else {
          mainGameUI.showInventoryWidget();
        }
        inventoryKeyBlock = true;
      }
    }
  }

  private void handleKeyReleasedEvent(KeyReleasedEvent e) {
    if (KeyboardUtils.isBuildMenuKey(e.getKey())) {
      buildKeyBlock = false;
    }

    if (KeyboardUtils.isProduceMenuKey(e.getKey())) {
      constructKeyBlock = false;
    }

    if (KeyboardUtils.isInventoryKey(e.getKey())) {
      inventoryKeyBlock = false;
    }
  }
}
