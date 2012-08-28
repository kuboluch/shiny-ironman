package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.*;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractActionItemController<T extends Item> implements ItemController<T>, UpdateQueue.UpdateController<AbstractActionItemController.ItemWrapper<T>> {

  public static final int RANGE = 16 * Sizes.BLOCK;

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

  @Autowired
  protected UpdateQueue updateQueue;

  @Autowired
  protected PlayerController playerController;

  @Autowired
  protected InputContainer inputContainer;

  protected Rectangle affectedRectangle;

  abstract protected boolean canPerformAction(int x, int y);

  abstract protected Rectangle getAffectedRectangle(int x, int y);

  abstract protected void startAction(T item);

  abstract protected void stopAction(T item);

  abstract protected void updateAction(T item, int delta);

  abstract protected boolean isActionCompleted();

  abstract protected void onAction();

  public boolean canPerformAction() {
    int x = Sizes.roundToBlockSizeX(affectedRectangle.getX());
    int y = Sizes.roundToBlockSizeY(affectedRectangle.getY());
    return canPerformAction(x, y);
  }

  @Override
  public void listen(T item, List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(item, mousePressedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      for (MouseDraggedEvent e : mouseDraggedEvents) {
        handleMouseDraggedEvent(item, e);
      }
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.collect(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      for (MouseReleasedEvent e : mouseReleasedEvents) {
        handleMouseReleasedEvent(item, e);
      }
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
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
      x = Sizes.roundToBlockSizeX(e.getLevelX());
      y = Sizes.roundToBlockSizeY(e.getLevelY());
      if (isInRange(x, y) && e.getButton() == Button.LEFT) {
        mpe = e;
        break;
      }
    }
    if (mpe == null) return;

    if (canPerformAction(x, y)) {
      affectedRectangle = getAffectedRectangle(x, y);
      startAction(item);
      updateQueue.add(getWrapper(item));
    }
  }

  public void handleMouseDraggedEvent(T item, MouseDraggedEvent event) {
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(event.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(event.getNewLevelY());
    handleMouseCoordChange(item, x, y);
  }

  public void handleScreenMovedEvent(T item, ScreenMovedEvent event) {
    if (!inputContainer.getInput().isMouseButtonDown(0)) {
      return;
    }
    int x = Sizes.roundToBlockSizeX(inputContainer.getInput().getMouseX() + event.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(inputContainer.getInput().getMouseY() + event.getNewShiftY());
    handleMouseCoordChange(item, x, y);
  }

  private ItemWrapper<T> getWrapper(T item) {
    return new ItemWrapper<T>(item, (Class<? extends UpdateQueue.UpdateController<ItemWrapper<T>>>)this.getClass());
  }

  private void handleMouseCoordChange(T item, int x, int y) {
    Rectangle rect = new Rectangle(x, y, 1, 1);
    if (affectedRectangle != null && (!affectedRectangle.intersects(rect) || !isInRange(x, y))) {
      updateQueue.remove(getWrapper(item));
      stopAction(item);
      affectedRectangle = null;
    }
    if (!isInRange(x, y)) {
      return;
    }

    if (affectedRectangle == null && canPerformAction(x, y)) {
      affectedRectangle = getAffectedRectangle(x, y);
      startAction(item);
      updateQueue.add(getWrapper(item));
    }
  }

  public void handleMouseReleasedEvent(T item, MouseReleasedEvent event) {
    if (affectedRectangle == null) {
      return;
    }
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(event.getLevelX());
    int y = Sizes.roundToBlockSizeY(event.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }

    updateQueue.remove(getWrapper(item));
    stopAction(item);
    affectedRectangle = null;
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
      updateQueue.remove(wrapper);
      stopAction(wrapper.getItem());
      affectedRectangle = null;
    }
  }
}
