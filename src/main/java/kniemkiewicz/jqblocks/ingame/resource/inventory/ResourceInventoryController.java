package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.controller.*;
import kniemkiewicz.jqblocks.ingame.controller.action.AbstractActionController;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.controller.event.EventListener;
import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObjectType;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceObject;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * User: kuboluch
 * Date: 31.07.12
 */
@Component
public class ResourceInventoryController implements EventListener {

  private static int DROP_RANGE = 16 * Sizes.BLOCK;

  @Autowired
  ResourceInventory inventory;

  @Autowired
  SpringBeanProvider provider;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  PlayerController playerController;

  @Autowired
  InputContainer inputContainer;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World objectKiller;

  @Autowired
  FreeFallController freeFallController;

  public boolean addItem(ResourceItem item) {
    if (inventory.add(item)) {
      return true;
    }
    return false;
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    List<KeyPressedEvent> keyPressedEvents = Collections3.filter(events, KeyPressedEvent.class);
    if (!keyPressedEvents.isEmpty()) {
      for (KeyPressedEvent e : keyPressedEvents) {
        handleKeyPressedEvent(e);
      }
    }

    List<MousePressedEvent> mousePressedEvents = Collections3.filter(events, MousePressedEvent.class);

    for (MousePressedEvent e : mousePressedEvents) {
      if (e.getButton() == Button.RIGHT) {
        handleMouseRightClickEvent(e);
      }
    }

    // TODO add and move to PlayerEquipmentController
    if (inventory.getSelectedItem() != null) {
      Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
      if (clazz != null) {
        ItemController controller = provider.getBean(clazz, true);
        controller.listen(inventory.getSelectedItem(), events);
      }
    }
  }

  private void handleKeyPressedEvent(KeyPressedEvent e) {
    if (KeyboardUtils.isFunctionKeyPressed(e.getKey())) {
      int f = KeyboardUtils.getPressedFunctionKey(e.getKey());
      if (f > 0 && f <= inventory.getSize()) {
        inventory.setSelectedIndex(f - 1);
        e.consume();
      }
    }

    if (KeyboardUtils.isDownKey(e.getKey())) {
      ResourceObject resourceObject = findNearestResourceObject();
      if (resourceObject != null) {
        if (inventory.add((ResourceItem) resourceObject.getItem())) {
          objectKiller.killMovingObject(resourceObject);
          e.consume();
        }
      }
    }
  }

  private void handleMouseRightClickEvent(MousePressedEvent e) {
    if (KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput())) {
      if (dropItem(e.getLevelX(), e.getLevelY())) {
        e.consume();
        return;
      }
    }
  }

  private ResourceObject findNearestResourceObject() {
    float playerX = playerController.getPlayer().getShape().getCenterX();
    float playerY = playerController.getPlayer().getShape().getCenterY();
    double lastDistance = -1;
    ResourceObject nearestResourceObject = null;

    for (PickableObject pickableObject : collisionController.<PickableObject>fullSearch(MovingObjects.PICKABLE, playerController.getPlayer().getShape())) {
      if (PickableObjectType.RESOURCE.equals(pickableObject.getType())) {
        double distanceToObject = GeometryUtils.distance(playerX, playerY, pickableObject.getShape().getCenterX(), pickableObject.getShape().getCenterY());
        if (lastDistance < 0 || distanceToObject < lastDistance) {
          nearestResourceObject = (ResourceObject) pickableObject;
        }
      }
    }

    return nearestResourceObject;
  }

  public void dropObject(DroppableObject dropObject) {
    addToWorld(dropObject);
    freeFallController.addCanFall(dropObject);
  }

  private boolean addToWorld(DroppableObject dropObject) {
    if (!movingObjects.add(dropObject, false)) return false;
    renderQueue.add(dropObject);
    return true;
  }

  private boolean dropItem(int x, int y) {
    if (inventory.getSelectedItem() == null) return false;
    if (!AbstractActionController.isInRange(x, y, playerController.getPlayer(), DROP_RANGE)) return false;
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
    if (clazz == null) return false;
    ItemController controller = provider.getBean(clazz, true);
    int dropItemX = Sizes.floorToBlockSizeX(x);
    int dropItemY = Sizes.floorToBlockSizeY(y);
    DroppableObject dropObject = controller.getObject(inventory.getSelectedItem(), dropItemX, dropItemY);
    if (dropObject == null) return false;
    if (conflictingObjectExists(dropObject.getShape())) return false;
    if (!solidBlocks.isOnSolidGround(dropObject.getShape())) return false;
    dropObject(dropObject);
    inventory.removeSelected();
    return true;
  }

  private boolean conflictingObjectExists(Shape shape) {
    Rectangle rect = new Rectangle(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    List<ResourceObject> resourceObjects =
        Collections3.filter(collisionController.fullSearch(MovingObjects.OBJECT_TYPES, rect), ResourceObject.class);
    return !resourceObjects.isEmpty();
  }
}
