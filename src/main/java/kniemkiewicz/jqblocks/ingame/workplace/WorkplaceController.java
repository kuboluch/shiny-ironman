package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceController implements EventListener {

  @Autowired
  MainGameUI mainGameUI;
  @Autowired
  InputContainer inputContainer;
  @Autowired
  RenderQueue renderQueue;
  @Autowired
  Backgrounds backgrounds;
  @Autowired
  SolidBlocks solidBlocks;
  @Autowired
  PlayerController playerController;

  List<Workplace> workplaces = new ArrayList<Workplace>();

  private Workplace selectedWorkplace;

  private PlaceableWorkplaceObject placableWorkplaceObject;

  public WorkplaceController(List<Workplace> workplaces) {
    this.workplaces = workplaces;
  }

  public List<Workplace> getWorkplaces() {
    return new ArrayList<Workplace>(workplaces);
  }

  public void changeSelected(Workplace selected) {
    selectedWorkplace = selected;
  }

  @Override
  public void listen(List<Event> events) {
    List<KeyPressedEvent> keyPressedEvents = Collections3.collect(events, KeyPressedEvent.class);
    if (!keyPressedEvents.isEmpty()) {
      for (KeyPressedEvent e : keyPressedEvents) {
        handleInteractKeyPressedEvent(e);
      }
    }

    if (selectedWorkplace == null) return;

    List<MouseMovedEvent> mouseMovedEvents = Collections3.collect(events, MouseMovedEvent.class);
    if (!mouseMovedEvents.isEmpty()) {
      for (MouseMovedEvent e : mouseMovedEvents) {
        handleMouseMovedEvent(e);
      }
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      for (MouseDraggedEvent e : mouseDraggedEvents) {
        handleMouseDraggedEvent(e);
      }
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      for (ScreenMovedEvent e : screenMovedEvents) {
        handleScreenMovedEvent(e);
      }
    }

    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      for (MousePressedEvent e : mousePressedEvents) {
        if (e.getButton() == Button.RIGHT) {
          handleMouseRightClickEvent(e);
          break;
        }
        if (e.getButton() == Button.LEFT) {
          handleMouseLeftClickEvent(e);
          break;
        }
      }
    }
  }

  private void handleMouseRightClickEvent(Event evt) {
    assert evt != null;
    stopPlacing();
    evt.consume();
  }

  private void handleMouseLeftClickEvent(Event evt) {
    assert evt != null;
    if (canBePlaced()) {
      place();
      stopPlacing();
    }
    evt.consume();
  }

  private void stopPlacing() {
    selectedWorkplace = null;
    renderQueue.remove(placableWorkplaceObject);
    placableWorkplaceObject = null;
    mainGameUI.resetWorkplaceWidget();
  }

  private void place() {
    renderQueue.remove(placableWorkplaceObject);
    backgrounds.add(placableWorkplaceObject.getBackgroundElement());
  }

  public void handleMouseMovedEvent(MouseMovedEvent event) {
    int x = Sizes.roundToBlockSizeX(event.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(event.getNewLevelY());
    handleMouseCoordChange(x, y);
    event.consume();
  }

  public void handleMouseDraggedEvent(MouseDraggedEvent event) {
    if (event.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(event.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(event.getNewLevelY());
    handleMouseCoordChange(x, y);
    event.consume();
  }

  public void handleScreenMovedEvent(ScreenMovedEvent event) {
    int x = Sizes.roundToBlockSizeX(inputContainer.getInput().getMouseX() + event.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(inputContainer.getInput().getMouseY() + event.getNewShiftY());
    handleMouseCoordChange(x, y);
    event.consume();
  }

  private void handleMouseCoordChange(int x, int y) {
    if (placableWorkplaceObject == null) {
      placableWorkplaceObject = selectedWorkplace.getPlaceableObject(x, y, this);
      renderQueue.add(placableWorkplaceObject);
    }
    placableWorkplaceObject.changeX(x);
    placableWorkplaceObject.changeY(y);
  }

  private void handleInteractKeyPressedEvent(KeyPressedEvent e) {
    if (KeyboardUtils.isInteractKeyPressed(e.getKey())) {
      Workplace workplace = findWorkplace(playerController.getPlayer().getShape());
      if (workplace != null) {
        workplace.interact();
        e.consume();
      }
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }

  public boolean canBePlaced() {
    if (solidBlocks.getBlocks().collidesWithNonEmpty(placableWorkplaceObject.getShape())) return false;
    if (!solidBlocks.isOnSolidGround(placableWorkplaceObject.getShape())) return false;
    return true;
  }

  private Workplace findWorkplace(Rectangle rect) {
    BackgroundElement backgroundElement = null;
    Iterator<BackgroundElement> it = backgrounds.intersects(rect);
    while (it.hasNext()) {
      backgroundElement = it.next();
      if (backgroundElement.isWorkplace()) {
        return ((WorkplaceBackgroundElement) backgroundElement).getWorkplace();
      }
    }
    return null;
  }
}
