package kniemkiewicz.jqblocks.ingame.input;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.input.event.*;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MouseInputEventBus implements MouseListener {

  @Autowired
  PointOfView pointOfView;

  final Object lock = new Object();

  List<InputEvent> events = new ArrayList<InputEvent>();

  final List<MouseInputListener> listeners = new ArrayList<MouseInputListener>();

  public void add(MouseInputListener mouseInputListener) {
    listeners.add(mouseInputListener);
  }

  public void update() {
    List<InputEvent> eventsForListener;
    synchronized (lock) {
      eventsForListener = events;
      events = new ArrayList<InputEvent>();
    }
    Collections.sort(eventsForListener, new InputEvent.TimeComparator());
    for (MouseInputListener l : listeners) {
      l.listen(eventsForListener);
    }
  }

  public void mousePressed(int button, int x, int y) {
    synchronized (lock) {
      events.add(new MousePressedEvent(button, x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y));
    }
  }

  public void mouseWheelMoved(int change) { }

  public void mouseClicked(int button, int x, int y, int clickCount) {
    // left clicks only, for now.
    if (button != 0) return;
    synchronized (lock) {
      events.add(new MouseClickEvent(button, x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y, clickCount));
    }
  }

  public void mouseReleased(int button, int x, int y) {
    synchronized (lock) {
      events.add(new MouseReleasedEvent(button, x + pointOfView.getShiftX(), y + pointOfView.getShiftY(), x, y));
    }
  }

  public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    synchronized (lock) {
      events.add(new MouseMovedEvent(oldx + pointOfView.getShiftX(), oldy + pointOfView.getShiftY(), oldx, oldy,
          newx + pointOfView.getShiftX(), newy + pointOfView.getShiftY(), newx, newy));
    }
  }

  public void mouseDragged(int oldx, int oldy, int newx, int newy) { }

  public void setInput(Input input) { }

  public boolean isAcceptingInput() {
    return true;
  }

  public void inputEnded() {  }

  public void inputStarted() {  }

}