package kniemkiewicz.jqblocks.ingame.action;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
public abstract class AbstractActionController implements EventListener {

  public static final int RANGE = 16 * Sizes.BLOCK;

  @Autowired
  protected UpdateQueue updateQueue;

  @Autowired
  protected PlayerController playerController;

  @Autowired
  protected InputContainer inputContainer;

  protected Rectangle affectedRectangle;

  abstract protected boolean canPerformAction(int x, int y);

  abstract protected Rectangle getAffectedRectangle(int x, int y);

  abstract protected void startAction();

  abstract protected void stopAction();

  abstract protected void updateAction(int delta);

  abstract protected boolean isActionCompleted();

  abstract protected void onAction();

  public boolean canPerformAction() {
    int x = Sizes.roundToBlockSizeX(affectedRectangle.getX());
    int y = Sizes.roundToBlockSizeY(affectedRectangle.getY());
    return canPerformAction(x, y);
  }

  @Override
  public void listen(List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      for (MousePressedEvent e : mousePressedEvents) {
        handleMousePressedEvent(e);
      }
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      for (MouseDraggedEvent e : mouseDraggedEvents) {
        handleMouseDraggedEvent(e);
      }
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.collect(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      for (MouseReleasedEvent e : mouseReleasedEvents) {
        handleMouseReleasedEvent(e);
      }
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      for (ScreenMovedEvent e : screenMovedEvents) {
        handleScreenMovedEvent(e);
      }
    }
  }

  public void handleMousePressedEvent(MousePressedEvent event) {
    int x = Sizes.roundToBlockSizeX(event.getLevelX());
    int y = Sizes.roundToBlockSizeY(event.getLevelY());
    if (isInRange(x, y) && event.getButton() == Button.LEFT) {
      if (canPerformAction(x, y)) {
        affectedRectangle = getAffectedRectangle(x, y);
        startAction();
        event.consume();
      }
    }
  }

  public void handleMouseDraggedEvent(MouseDraggedEvent event) {
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(event.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(event.getNewLevelY());
    handleMouseCoordChange(x, y);
    if (affectedRectangle != null) event.consume();
  }

  public void handleScreenMovedEvent(ScreenMovedEvent event) {
    if (!inputContainer.getInput().isMouseButtonDown(0)) {
      return;
    }
    int x = Sizes.roundToBlockSizeX(inputContainer.getInput().getMouseX() + event.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(inputContainer.getInput().getMouseY() + event.getNewShiftY());
    handleMouseCoordChange(x, y);
    if (affectedRectangle != null) event.consume();
  }

  private void handleMouseCoordChange(int x, int y) {
    Rectangle rect = new Rectangle(x, y, 1, 1);
    if (affectedRectangle != null && (!affectedRectangle.intersects(rect) || !isInRange(x, y))) {
      stopAction();
      affectedRectangle = null;
    }
    if (!isInRange(x, y)) {
      return;
    }

    if (affectedRectangle == null && canPerformAction(x, y)) {
      affectedRectangle = getAffectedRectangle(x, y);
      startAction();
    }
  }

  public void handleMouseReleasedEvent(MouseReleasedEvent event) {
    if (affectedRectangle == null) {
      return;
    }
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(event.getLevelX());
    int y = Sizes.roundToBlockSizeY(event.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }

    stopAction();
    affectedRectangle = null;
  }

  public static boolean isInRange(int x, int y, Player player, int range) {
    float px = player.getFullXYMovement().getX();
    float py = player.getFullXYMovement().getY();
    return  (px - x) * (px - x) + (py - y) * (py - y) < range * range;
  }

  public boolean isInRange(int x, int y) {
    return isInRange(x, y, playerController.getPlayer(), RANGE);
  }

  public void update(int delta) {
    if (affectedRectangle == null) {
      return;
    }
    updateAction(delta);
    if (isActionCompleted()) {
      if (canPerformAction()) {
        onAction();
      }
      stopAction();
      affectedRectangle = null;
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }
}
