package kniemkiewicz.jqblocks.ingame.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

@Component
public class MouseInputEventBus implements MouseListener {

  @Autowired
  EventBus eventBus;

  @Autowired
  PointOfView pointOfView;

  final Object lock = new Object();

  List<InputEvent> events = new ArrayList<InputEvent>();

  EnumSet<Button> pressedButtons = EnumSet.noneOf(Button.class);

  final List<MouseInputListener> listeners = new ArrayList<MouseInputListener>();

  public void add(MouseInputListener mouseInputListener) {
    listeners.add(mouseInputListener);
  }

  public void update() {
    List<InputEvent> eventsToBroadcast;
    synchronized (lock) {
      eventsToBroadcast = events;
      events = new ArrayList<InputEvent>();
    }
    Collections.sort(eventsToBroadcast, new InputEvent.TimeComparator());
    eventBus.broadcast(eventsToBroadcast);
  }

  public void mousePressed(int button, int x, int y) {
    synchronized (lock) {
      Button b = Button.parseInt(button);
      events.add(new MousePressedEvent(b, x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y));
      pressedButtons.add(b);
    }
  }

  public void mouseWheelMoved(int change) { }

  public void mouseClicked(int button, int x, int y, int clickCount) {
    synchronized (lock) {
      events.add(new MouseClickEvent(Button.parseInt(button), x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y, clickCount));
    }
  }

  public void mouseReleased(int button, int x, int y) {
    synchronized (lock) {
      Button b = Button.parseInt(button);
      events.add(new MouseReleasedEvent(b, x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y));
      pressedButtons.remove(b);
    }
  }

  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    synchronized (lock) {
      events.add(new MouseMovedEvent(oldx + pointOfView.getShiftX(), oldy + pointOfView.getShiftY(), oldx, oldy,
          newx + pointOfView.getShiftX(), newy + pointOfView.getShiftY(), newx, newy));
    }
  }

  public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    synchronized (lock) {
      for (Button b : pressedButtons) {
        events.add(new MouseDraggedEvent(oldx + pointOfView.getShiftX(), oldy + pointOfView.getShiftY(), oldx, oldy,
            newx + pointOfView.getShiftX(), newy + pointOfView.getShiftY(), newx, newy, b));
      }
    }
  }

  public void setInput(Input input) { }

  public boolean isAcceptingInput() {
    return true;
  }

  public void inputEnded() {  }

  public void inputStarted() {  }

}