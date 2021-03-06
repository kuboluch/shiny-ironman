package kniemkiewicz.jqblocks.ingame.inventory;

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
import kniemkiewicz.jqblocks.ingame.controller.event.input.mouse.MouseWheelEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.QuickItemInventory;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObjectType;
import kniemkiewicz.jqblocks.ingame.hud.inventory.ItemDragController;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
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
  QuickItemInventory itemInventory;

  @Autowired
  BackpackInventory backpackInventory;

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

  @Autowired
  ItemDragController itemDragController;

  public boolean addItem(Item item) {
    if (itemInventory.add(item)) {
      return true;
    }
    if (backpackInventory.add(item)) {
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
    if (!mousePressedEvents.isEmpty()) {
      for (MousePressedEvent e : mousePressedEvents) {
        if (e.getButton() == Button.RIGHT) {
          handleMouseRightClickEvent(e);
        }
      }
    }

    List<MouseWheelEvent> mouseWheelEvents = Collections3.filter(events, MouseWheelEvent.class);
    if (!mouseWheelEvents.isEmpty()) {
      for (MouseWheelEvent e : mouseWheelEvents) {
        handleMouseWheelEvent(e);
      }
    }

    // TODO add and move to PlayerEquipmentController
    if (itemInventory.getSelectedItem() != null && !itemDragController.isDragging()) {
      ItemController controller = getItemController();
      if (controller != null) {
        controller.listen(itemInventory.getSelectedItem(), events);
      }
    }
  }

  private ItemController<?> getItemController() {
    Class<? extends ItemController> clazz = itemInventory.getSelectedItem().getItemController();
    if (clazz != null) {
      return provider.getBean(clazz, true);
    }
    return null;
  }

  private void handleKeyPressedEvent(KeyPressedEvent e) {
    if (KeyboardUtils.isNumericKeyPressed(e.getKey())) {
      int k = KeyboardUtils.getPressedNumericKey(e.getKey());
      if (k == 0) {
        setInventorySelectedIndex(9);
        e.consume();
      } else if (k > 0 && k <= itemInventory.getSize()) {
        setInventorySelectedIndex(k - 1);
        e.consume();
      }
    }

    if (KeyboardUtils.isDownKey(e.getKey())) {
      PickableObject object = findNearestPickableObject();
      if (object != null) {
        if (itemInventory.add(object.getItem())) {
          objectKiller.killMovingObject(object);
          e.consume();
        }
      }
    }
  }

  public boolean setInventorySelectedIndex(int x) {
    ItemController controller = getItemController();
    if (controller != null && !controller.canDeselectItem(itemInventory.getSelectedItem())) return false;
    itemInventory.setSelectedIndex(x);
    return true;
  }

  private void handleMouseRightClickEvent(MousePressedEvent e) {
    if (!KeyboardUtils.isResourceInventoryKeyPressed(inputContainer.getInput())) {
      if (dropItem(itemInventory.getSelectedItem(), e.getLevelX(), e.getLevelY())) {
        itemInventory.removeSelected();
        e.consume();
        return;
      }
    }
  }

  private void handleMouseWheelEvent(MouseWheelEvent e) {
    if (e.getDelta() < 0) {
      int selectedIndex = itemInventory.getSelectedIndex();
      selectedIndex--;
      if (selectedIndex < 0) {
        selectedIndex = itemInventory.getSize() - 1;
      }
      setInventorySelectedIndex(selectedIndex);
      e.consume();
      return;
    } else if (e.getDelta() > 0) {
      int selectedIndex = itemInventory.getSelectedIndex();
      selectedIndex++;
      if (selectedIndex > itemInventory.getSize() - 1) {
        selectedIndex = 0;
      }
      setInventorySelectedIndex(selectedIndex);
      e.consume();
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

  public void dropObject(DroppableObject dropObject) {
    addToWorld(dropObject);
    freeFallController.addCanFall(dropObject);
  }

  private boolean addToWorld(DroppableObject dropObject) {
    if (!movingObjects.add(dropObject, false)) return false;
    renderQueue.add(dropObject);
    return true;
  }

  public boolean dropItem(Item item, int x, int y) {
    if (item == null) return false;
    if (!AbstractActionController.isInRange(x, y, playerController.getPlayer(), DROP_RANGE)) return false;
    ItemController controller = getItemController();
    if (controller == null) return false;
    if (!controller.canDeselectItem(item)) return false;
    DroppableObject dropObject = controller.getObject(item, x, y);
    if (dropObject == null) return false;
    if (solidBlocks.isColliding(dropObject.getShape())) return false;
    dropObject(dropObject);
    return true;
  }
}
