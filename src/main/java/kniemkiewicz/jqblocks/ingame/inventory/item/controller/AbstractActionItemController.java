package kniemkiewicz.jqblocks.ingame.inventory.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
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
import kniemkiewicz.jqblocks.ingame.controller.InputContainer;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.hud.inventory.ItemDragController;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractActionItemController<T extends Item>
    implements ItemController<T>, EventListener, UpdateQueue.UpdateController<AbstractActionItemController.ItemWrapper<T>> {

  public static final int RANGE = 16 * Sizes.BLOCK;

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

  protected Rectangle affectedRectangle;

  T item;

  @PostConstruct
  public void init() {
    eventBus.addListener(this);
  }

  abstract protected boolean canPerformAction(int x, int y);

  abstract protected Rectangle getAffectedRectangle(int x, int y);

  abstract protected void startAction();

  abstract protected void stopAction();

  abstract protected void updateAction(T item, int delta);

  abstract protected boolean isActionCompleted();

  abstract protected void onAction();

  private void start(T item, int x, int y) {
    affectedRectangle = getAffectedRectangle(x, y);
    startAction();
    updateQueue.add(getWrapper(item));
    this.item = item;
  }

  private void stop() {
    updateQueue.remove(getWrapper(item));
    this.item = null;
    stopAction();
    affectedRectangle = null;
  }

  public boolean canPerformAction() {
    if (itemDragController.isDragging()) {
      return false;
    }
    int x = Sizes.roundToBlockSizeX(affectedRectangle.getX());
    int y = Sizes.roundToBlockSizeY(affectedRectangle.getY());
    return canPerformAction(x, y);
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) SelectedItemChangeEvent.class, (Class) ActionStartedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    if (!events.isEmpty()) {
      if (affectedRectangle != null) {
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
      x = Sizes.floorToBlockSizeX(e.getLevelX());
      y = Sizes.floorToBlockSizeY(e.getLevelY());
      if (isInRange(x, y) && e.getButton() == Button.LEFT) {
        mpe = e;
        break;
      }
    }
    if (mpe == null) return;

    if (canPerformAction(x, y)) {
      start(item, x, y);
    }
  }

  public void handleMouseDraggedEvent(T item, MouseDraggedEvent event) {
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.floorToBlockSizeX(event.getNewLevelX());
    int y = Sizes.floorToBlockSizeY(event.getNewLevelY());
    handleMouseCoordChange(item, x, y);
  }

  public void handleScreenMovedEvent(T item, ScreenMovedEvent event) {
    if (!inputContainer.getInput().isMouseButtonDown(0)) {
      return;
    }
    int x = Sizes.floorToBlockSizeX(inputContainer.getInput().getMouseX() + event.getNewShiftX());
    int y = Sizes.floorToBlockSizeY(inputContainer.getInput().getMouseY() + event.getNewShiftY());
    handleMouseCoordChange(item, x, y);
  }

  private void handleMouseCoordChange(T item, int x, int y) {
    Rectangle rect = new Rectangle(x, y, 1, 1);
    if (affectedRectangle != null && (!affectedRectangle.intersects(rect) || !isInRange(x, y))) {
      stop();
    }
    if (!isInRange(x, y)) {
      return;
    }

    if (affectedRectangle == null && canPerformAction(x, y)) {
      start(item, x, y);
    }
  }

  public void handleMouseReleasedEvent(T item, MouseReleasedEvent event) {
    if (affectedRectangle == null) {
      return;
    }
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.floorToBlockSizeX(event.getLevelX());
    int y = Sizes.floorToBlockSizeY(event.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }

    stop();
  }

  public boolean isInRange(int x, int y) {
    return AbstractActionController.isInRange(x, y, playerController.getPlayer(), RANGE);
  }

  @Override
  public void update(ItemWrapper<T> wrapper, int delta) {
    if (affectedRectangle == null) {
      return;
    }
    updateAction(wrapper.getItem(), delta);
    if (isActionCompleted()) {
      if (canPerformAction()) {
        onAction();
      }
      stop();
    }
  }

  private ItemWrapper<T> getWrapper(T item) {
    return new ItemWrapper<T>(item, (Class<? extends UpdateQueue.UpdateController<ItemWrapper<T>>>)this.getClass());
  }

  static class ItemWrapper<E extends Item> implements UpdateQueue.ToBeUpdated<ItemWrapper<E>> {

    final E item;
    final Class<? extends UpdateQueue.UpdateController<ItemWrapper<E>>> beanName;

    ItemWrapper(E item, Class<? extends UpdateQueue.UpdateController<ItemWrapper<E>>> beanName) {
      this.item = item;
      this.beanName = beanName;
    }

    public E getItem() {
      return item;
    }

    @Override
    public Class<? extends UpdateQueue.UpdateController<ItemWrapper<E>>> getUpdateController() {
      return beanName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ItemWrapper that = (ItemWrapper) o;

      if (!item.equals(that.item)) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return item.hashCode();
    }
  }
}
