package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractActionItemController<T extends UpdateQueue.ToBeUpdated<T> & Item> implements ItemController<T>, UpdateQueue.UpdateController<T> {
  public static final int RANGE = 75;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  PlayerController playerController;

  @Autowired
  InputContainer inputContainer;

  Rectangle affectedRectangle;

  abstract boolean canPerformAction(int x, int y);

  abstract Rectangle getAffectedRectangle(int x, int y);

  abstract void startAction(T item);

  abstract void stopAction(T item);

  abstract void updateAction(T item, int delta);

  abstract boolean isActionCompleted();

  abstract void onAction();

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
      handleMouseDraggedEvent(item, mouseDraggedEvents);
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.collect(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      handleMouseReleasedEvent(item, mouseReleasedEvents);
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      handleScreenMovedEvent(item, screenMovedEvents);
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
      updateQueue.add(item);
    }
  }

  public void handleMouseDraggedEvent(T item, List<MouseDraggedEvent> mouseDraggedEvents) {
    assert mouseDraggedEvents.size() > 0;
    // TODO: there may be more than 1 event!
    MouseDraggedEvent mde = mouseDraggedEvents.get(0);
    if (mde.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    handleMouseCoordChange(item, x, y);
  }

  public void handleScreenMovedEvent(T item, List<ScreenMovedEvent> screenMovedEvents) {
    if (!inputContainer.getInput().isMouseButtonDown(0)) {
      return;
    }
    // TODO: there may be more than 1 event!
    assert screenMovedEvents.size() > 0;
    ScreenMovedEvent sme = screenMovedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(inputContainer.getInput().getMouseX() + sme.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(inputContainer.getInput().getMouseY() + sme.getNewShiftY());
    handleMouseCoordChange(item, x, y);
  }

  private void handleMouseCoordChange(T item, int x, int y) {
    Rectangle rect = new Rectangle(x, y, 1, 1);
    if (affectedRectangle != null && (!affectedRectangle.intersects(rect) || !isInRange(x, y))) {
      updateQueue.remove(item);
      stopAction(item);
      affectedRectangle = null;
    }
    if (!isInRange(x, y)) {
      return;
    }

    if (affectedRectangle == null && canPerformAction(x, y)) {
      affectedRectangle = getAffectedRectangle(x, y);
      startAction(item);
      updateQueue.add(item);
    }
  }

  public void handleMouseReleasedEvent(T item, List<MouseReleasedEvent> mouseReleasedEvents) {
    if (affectedRectangle == null) {
      return;
    }
    // TODO: there may be more than 1 event!
    assert mouseReleasedEvents.size() > 0;
    MouseReleasedEvent mre = mouseReleasedEvents.get(0);
    if (mre.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(mre.getLevelX());
    int y = Sizes.roundToBlockSizeY(mre.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }

    updateQueue.remove(item);
    stopAction(item);
    affectedRectangle = null;
  }

  public static boolean isInRange(int x, int y, Player player, int range) {
    float px = player.getXMovement().getPos();
    float py = player.getYMovement().getPos();
    return  (px - x) * (px - x) + (py - y) * (py - y) < range * range;
  }

  public boolean isInRange(int x, int y) {
    return isInRange(x, y, playerController.getPlayer(), RANGE);
  }

  @Override
  public void update(T item, int delta) {
    if (affectedRectangle == null) {
      return;
    }
    updateAction(item, delta);
    if (isActionCompleted()) {
      if (canPerformAction()) {
        onAction();
      }
      updateQueue.remove(item);
      stopAction(item);
      affectedRectangle = null;
    }
  }
}
