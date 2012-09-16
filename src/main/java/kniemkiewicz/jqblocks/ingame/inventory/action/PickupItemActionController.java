package kniemkiewicz.jqblocks.ingame.inventory.action;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.action.ActionStartedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObjectType;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventoryController;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import kniemkiewicz.jqblocks.ingame.ui.inventory.ItemDragController;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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

  @Autowired
  InventoryController inventoryController;

  @Autowired
  ResourceInventoryController resourceInventoryController;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  World objectKiller;

  PickableObject affectedObject = null;

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) MousePressedEvent.class, (Class) MouseDraggedEvent.class, (Class) MouseReleasedEvent.class);
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
        affectedObject = pickableObject;
        eventBus.broadcast(new ActionStartedEvent());
        event.consume();
      }
    }
  }

  private void handleMouseDraggedEvent(MouseDraggedEvent event) {
    if (affectedObject != null) {
      int x = Sizes.roundToBlockSizeX(event.getOldLevelX());
      int y = Sizes.roundToBlockSizeY(event.getOldLevelY());
      if (isInRange(x, y) && event.getButton().equals(Button.LEFT)) {
        PickableObject pickableObject = findPickableObject(x, y);
        if (pickableObject == null) {
          pickableObject = findPickableResourceObject(x, y);
        }
        if (pickableObject != null) {
          itemDragController.dragStarted(pickableObject);
          event.consume();
        }
      }
    }
  }

  private void handleMouseReleasedEvent(MouseReleasedEvent event) {
    if (affectedObject != null) {
      int x = Sizes.roundToBlockSizeX(event.getLevelX());
      int y = Sizes.roundToBlockSizeY(event.getLevelY());
      if (isInRange(x, y) && event.getButton().equals(Button.LEFT)) {
        PickableObject pickableObject = findPickableObject(x, y);
        if (pickableObject == null) {
          pickableObject = findPickableResourceObject(x, y);
        }
        if (pickableObject != null && pickableObject.equals(affectedObject)) {
          if (pickableObject.getType().equals(PickableObjectType.ACTION)) {
            if (!inventoryController.addItem(pickableObject.getItem())) {
              Shape dropShape = getDropShape(pickableObject);
              inventoryController.dropItem(pickableObject.getItem(), (int) dropShape.getX(), (int) dropShape.getY());
            }
          } else if (pickableObject.getType().equals(PickableObjectType.RESOURCE)) {
            if (!resourceInventoryController.addItem((ResourceItem) pickableObject.getItem())) {
              Shape dropShape = getDropShape(pickableObject);
              inventoryController.dropItem(pickableObject.getItem(), (int) dropShape.getX(), (int) dropShape.getY());
            }
          }
          objectKiller.killMovingObject(pickableObject);
          event.consume();
        }
      }
      affectedObject = null;
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

  private Shape getDropShape(PickableObject pickableObject) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(pickableObject.getShape());
    rect.setY(rect.getY() - Sizes.BLOCK);
    if (!solidBlocks.isColliding(rect)) {
      return rect;
    }
    return pickableObject.getShape();
  }
}
