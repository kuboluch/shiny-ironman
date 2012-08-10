package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.util.Collections3;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
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
    if (selectedWorkplace == null) return;

    List<MouseMovedEvent> mouseMovedEvents = Collections3.collect(events, MouseMovedEvent.class);
    if (!mouseMovedEvents.isEmpty()) {
      handleMouseMovedEvent(mouseMovedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      handleMouseDraggedEvent(mouseDraggedEvents);
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      handleScreenMovedEvent(screenMovedEvents);
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
    place();
    stopPlacing();
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

  public void handleMouseMovedEvent(List<MouseMovedEvent> mouseMovedEvents) {
    assert mouseMovedEvents.size() > 0;
    // TODO: there may be more than 1 event!
    MouseMovedEvent mde = mouseMovedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    handleMouseCoordChange(x, y);
  }

  public void handleMouseDraggedEvent(List<MouseDraggedEvent> mouseDraggedEvents) {
    assert mouseDraggedEvents.size() > 0;
    // TODO: there may be more than 1 event!
    MouseDraggedEvent mde = mouseDraggedEvents.get(0);
    if (mde.getButton() != Button.LEFT) return;
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    handleMouseCoordChange(x, y);
  }

  public void handleScreenMovedEvent(List<ScreenMovedEvent> screenMovedEvents) {
    // TODO: there may be more than 1 event!
    assert screenMovedEvents.size() > 0;
    ScreenMovedEvent sme = screenMovedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(inputContainer.getInput().getMouseX() + sme.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(inputContainer.getInput().getMouseY() + sme.getNewShiftY());
    handleMouseCoordChange(x, y);
  }

  private void handleMouseCoordChange(int x, int y) {
    if (placableWorkplaceObject == null) {
      placableWorkplaceObject = selectedWorkplace.getPlaceableObject(x, y);
      renderQueue.add(placableWorkplaceObject);
    } else {
    placableWorkplaceObject.changeX(x);
    placableWorkplaceObject.changeY(y);
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }
}
