package kniemkiewicz.jqblocks.ingame.controller.event.input.keyboard;

import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
@Component
public class KeyboardInputEventBus {

  @Autowired
  EventBus eventBus;

  final Object lock = new Object();

  List<InputEvent> events = new ArrayList<InputEvent>();

  public void update() {
    List<InputEvent> eventsToBroadcast;
    synchronized (lock) {
      eventsToBroadcast = events;
      events = new ArrayList<InputEvent>();
    }
    Collections.sort(eventsToBroadcast, new InputEvent.TimeComparator());
    eventBus.broadcast(eventsToBroadcast);
  }

  /**
	 * Notification that a key was pressed
	 *
	 * @param key The key code that was pressed (@see org.newdawn.slick.Input)
	 * @param c The character of the key that was pressed
	 */
  public void keyPressed(int key, char c) {
    synchronized (lock) {
      events.add(new KeyPressedEvent(key, c));
    }
  }

  /**
	 * Notification that a key was released
	 *
	 * @param key The key code that was released (@see org.newdawn.slick.Input)
	 * @param c The character of the key that was released
	 */
  public void keyReleased(int key, char c) {
    synchronized (lock) {
      events.add(new KeyReleasedEvent(key, c));
    }
  }
}
