package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;
import kniemkiewicz.jqblocks.ingame.event.input.keyboard.KeyPressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.InputContainer;
import kniemkiewicz.jqblocks.ingame.item.ItemInventory;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObjectType;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
@Component
public class InventoryController implements EventListener {

  private static int DROP_RANGE = 16 * Sizes.BLOCK;

  @Autowired
  ItemInventory inventory;

  @Autowired
  SpringBeanProvider provider;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  PlayerController playerController;

  @Autowired
  InputContainer inputContainer;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World objectKiller;

  public void listen(Input input, int delta) {

  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) InputEvent.class, (Class) ScreenMovedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    List<KeyPressedEvent> keyPressedEvents = Collections3.collect(events, KeyPressedEvent.class);
    if (!keyPressedEvents.isEmpty()) {
      for (KeyPressedEvent e : keyPressedEvents) {
        handleKeyPressedEvent(e);
      }
    }

    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      for (MousePressedEvent e : mousePressedEvents) {
        if (e.getButton() == Button.RIGHT) {
          handleMouseRightClickEvent(e);
        }
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
    if (KeyboardUtils.isNumericKeyPressed(e.getKey())) {
      int k = KeyboardUtils.getPressedNumericKey(e.getKey());
      if (k == 0) {
        inventory.setSelectedIndex(9);
        e.consume();
      } else if (k > 0 && k <= inventory.getSize()) {
        inventory.setSelectedIndex(k - 1);
        e.consume();
      }
    }

    if (KeyboardUtils.isDownPressed(e.getKey())) {
      PickableObject resourceObject = findNearestPickableObject();
      if (resourceObject != null) {
        if (inventory.add(resourceObject.getItem())) {
          objectKiller.killMovingObject(resourceObject);
          e.consume();
        }
      }
    }
  }

  private void handleMouseRightClickEvent(MousePressedEvent e) {
    if (!KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput())) {
      if (dropItem(e.getLevelX(), e.getLevelY())) {
        e.consume();
        return;
      }
    }
  }

  private PickableObject findNearestPickableObject() {
    float playerX = playerController.getPlayer().getShape().getCenterX();
    float playerY = playerController.getPlayer().getShape().getCenterY();
    double lastDistance = -1;
    PickableObject nearestPickableObject = null;

    for (PickableObject pickableObject : collisionController.<PickableObject>fullSearch(MovingObjects.PICKABLE, playerController.getPlayer().getShape())) {
      if (PickableObjectType.ACTION.equals(pickableObject.getType())) {
        double distanceToObject = GeometryUtils.distance(playerX, playerY, pickableObject.getShape().getCenterX(), pickableObject.getShape().getCenterY());
        if (lastDistance < 0 || distanceToObject < lastDistance) {
          nearestPickableObject = pickableObject;
        }
      }
    }

    return nearestPickableObject;
  }

  public void dropObject(MovingPhysicalObject dropObject) {
    Shape shape = dropObject.getShape();
    dropObject.setY((int) (solidBlocks.getBlocks().getUnscaledDropHeight(shape) - shape.getHeight() - 1));
  }

  private boolean dropItem(int x, int y) {
    if (inventory.getSelectedItem() == null) return false;
    if (!AbstractActionItemController.isInRange(x, y, playerController.getPlayer(), DROP_RANGE)) return false;
    Class<? extends ItemController> clazz = inventory.getSelectedItem().getItemController();
    if (clazz == null) return false;
    ItemController controller = provider.getBean(clazz, true);
    MovingPhysicalObject dropObject = controller.getDropObject(inventory.getSelectedItem(), x, y);
    if (dropObject == null) return false;
    dropObject(dropObject);
    inventory.removeSelectedItem();
    return true;
  }
}
