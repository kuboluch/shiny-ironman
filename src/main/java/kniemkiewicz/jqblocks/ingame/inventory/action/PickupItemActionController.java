package kniemkiewicz.jqblocks.ingame.inventory.action;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.action.ActionStartedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObjectType;
import kniemkiewicz.jqblocks.ingame.ui.inventory.ItemDragController;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * User: qba
 * Date: 10.09.12
 */
@Component
public class PickupItemActionController implements EventListener {

  public static final int RANGE = 16 * Sizes.BLOCK;

  @Autowired
  EventBus eventBus;

  @Autowired
  PlayerController playerController;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  CollisionController collisionController;

  @Autowired
  ItemDragController itemDragController;

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) MousePressedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      for (MousePressedEvent e : mousePressedEvents) {
        handleMousePressedEvent(e);
      }
    }
  }

  private void handleMousePressedEvent(MousePressedEvent event) {
    int x = Sizes.roundToBlockSizeX(event.getLevelX());
    int y = Sizes.roundToBlockSizeY(event.getLevelY());
    if (isInRange(x, y) && event.getButton().equals(Button.LEFT)) {
      PickableObject pickableObject = findPickableObject(x, y);
      if (pickableObject == null) {
        pickableObject = findPickableResourceObject(x, y);
      }
      if (pickableObject != null) {
        itemDragController.dragStarted(pickableObject);
        eventBus.broadcast(new ActionStartedEvent());
        event.consume();
      }
    }
  }

  private boolean isInRange(int x, int y) {
    float playerX = playerController.getPlayer().getXYMovement().getX();
    float playerY = playerController.getPlayer().getXYMovement().getY();
    return GeometryUtils.distance(x, y, playerX, playerY) < RANGE;
  }

  private PickableObject findPickableObject(int x, int y) {
    PickableObject result = null;
    for (PickableObject pickableObject : collisionController.<PickableObject>fullSearch(MovingObjects.PICKABLE, new Rectangle(x, y, 1, 1))) {
      if (PickableObjectType.ACTION.equals(pickableObject.getType())) {
        result = pickableObject;
        break;
      }
    }
    return result;
  }

  private PickableObject findPickableResourceObject(int x, int y) {
    PickableObject result = null;
    for (PickableObject pickableObject : collisionController.<PickableObject>fullSearch(MovingObjects.PICKABLE, new Rectangle(x, y, 1, 1))) {
      if (PickableObjectType.RESOURCE.equals(pickableObject.getType())) {
        result = pickableObject;
        break;
      }
    }
    return result;
  }
}
