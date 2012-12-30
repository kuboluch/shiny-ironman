package kniemkiewicz.jqblocks.ingame.inventory.item.controller;

import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.InputContainer;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.TimeController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.EventListener;
import kniemkiewicz.jqblocks.ingame.controller.event.action.ActionStartedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.inventory.SelectedItemChangeEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.hud.inventory.ItemDragController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.Vector2i;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractActionItemController<T extends Item>
    implements ItemController<T>, EventListener, UpdateQueue.UpdateController<ActionItemWrapper<T>> {

  @Autowired
  protected UpdateQueue updateQueue;

  @Autowired
  protected PlayerController playerController;

  @Autowired
  protected InputContainer inputContainer;

  @Autowired
  protected ItemDragController itemDragController;

  @Autowired
  EventBus eventBus;

  @Autowired
  TimeController timeController;

  T item;

  Vector2i xy;

  boolean stopTriggered = false;
  long startTime = 0;
  long minDuration = 0;

  @PostConstruct
  public void init() {
    eventBus.addListener(this);
  }

  abstract protected boolean canPerformAction(int x, int y);

  abstract protected void startAction();

  abstract protected void stopAction();

  abstract protected void updateAction(T item, int delta);

  abstract protected boolean isActionCompleted();

  private void start(T item, int x, int y) {
    if (xy != null) {
      return;
    }
    xy = new Vector2i(x ,y);
    startAction();
    updateQueue.add(getWrapper(item));
    this.item = item;
    this.startTime = timeController.getTime();
    stopTriggered = false;
  }

  private void stop() {
    if (xy == null) {
      return;
    }
    stopTriggered = true;
    if (startTime + minDuration > timeController.getTime()) {
      return;
    }
    updateQueue.remove(getWrapper(item));
    this.item = null;
    stopAction();
    xy = null;
  }

  public boolean canPerformAction() {
    return !itemDragController.isDragging() && canPerformAction(xy.getX(), xy.getY());
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) SelectedItemChangeEvent.class, (Class) ActionStartedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    if (!events.isEmpty()) {
      if (item != null) {
        stop();
      }
    }
  }

  @Override
  public void listen(T item, List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.filter(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(item, mousePressedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.filter(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      for (MouseDraggedEvent e : mouseDraggedEvents) {
        handleMouseDraggedEvent(item, e);
      }
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.filter(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      for (MouseReleasedEvent e : mouseReleasedEvents) {
        handleMouseReleasedEvent(item, e);
      }
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.filter(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      for (ScreenMovedEvent e : screenMovedEvents) {
        handleScreenMovedEvent(item, e);
      }
    }
  }

  public void handleMousePressedEvent(T item, List<MousePressedEvent> mousePressedEvents) {
    assert mousePressedEvents.size() > 0;
    MousePressedEvent mpe = null;
    int x = 0;
    int y = 0;
    for (MousePressedEvent e : mousePressedEvents) {
      x = e.getLevelX();
      y = e.getLevelY();
      if (e.getButton() == Button.LEFT && canPerformAction(e.getLevelX(), e.getLevelY())) {
        mpe = e;
        break;
      }
    }
    if (mpe == null) return;
    start(item, x, y);
  }

  public void handleMouseDraggedEvent(T item, MouseDraggedEvent event) {
    if (event.getButton() != Button.LEFT) return;
    handleMouseCoordChange(item, event.getNewLevelX(), event.getNewLevelY());
  }

  public void handleScreenMovedEvent(T item, ScreenMovedEvent event) {
    if (!inputContainer.getInput().isMouseButtonDown(0)) {
      return;
    }
    handleMouseCoordChange(item, inputContainer.getInput().getMouseX() + event.getNewShiftX(), inputContainer.getInput().getMouseY() + event.getNewShiftY());
  }

  private void handleMouseCoordChange(T item, int x, int y) {
    if (xy == null) return;
    if (!canPerformAction(x, y)) {
      stop();
      return;
    }
    xy.setX(x);
    xy.setY(y);
  }

  public void handleMouseReleasedEvent(T item, MouseReleasedEvent event) {
    if (xy == null || event.getButton() != Button.LEFT) {
      return;
    }
    stop();
  }

  @Override
  public void update(ActionItemWrapper<T> wrapper, int delta) {
    if (xy == null) {
      return;
    }
    updateAction(wrapper.getItem(), delta);
    if (stopTriggered || isActionCompleted()) {
      stop();
    }
  }

  @Override
  public boolean canDeselectItem(T item) {
    return xy == null;
  }

  private ActionItemWrapper<T> getWrapper(T item) {
    return new ActionItemWrapper<T>(item, (Class<? extends UpdateQueue.UpdateController<ActionItemWrapper<T>>>)this.getClass());
  }

  public void setMinDuration(long minDuration) {
    this.minDuration = minDuration;
  }
}
