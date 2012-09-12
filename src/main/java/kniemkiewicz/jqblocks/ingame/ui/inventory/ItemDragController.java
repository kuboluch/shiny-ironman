package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.ui.Initializable;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.DragListener;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.DraggableSlot;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.ItemSlot;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.PickupItemSlot;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class ItemDragController implements DragListener<Item>, Initializable {

  @Autowired
  MainGameUI mainGameUI;

  @Autowired
  InventoryController inventoryController;

  @Autowired
  QuickItemInventoryPanel quickItemInventoryPanel;

  @Autowired
  BackpackInventoryPanel backpackInventoryPanel;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  World objectKiller;

  private GUI gui;

  private PickupItemSlot pickupSlot = new PickupItemSlot();
  private DraggableSlot<Item> dragSlot;
  private DraggableSlot<Item> dropSlot;

  private PickableObject pickableObject;

  @Override
  public void init(GUI gui) {
    this.gui = gui;
    mainGameUI.getRootPane().add(pickupSlot);
    pickupSlot.setListener(this);
    pickupSlot.setSpringBeanProvider(springBeanProvider);
    pickupSlot.setVisible(false);
  }

  public void dragStarted(PickableObject pickableObject) {
    checkNotNull(pickableObject);
    this.pickableObject = pickableObject;
    pickupSlot.setModel(pickableObject.getItem());
    int x = (int) (pickableObject.getShape().getX() - pointOfView.getShiftX());
    int y = (int) (pickableObject.getShape().getY() - pointOfView.getShiftY());
    pickupSlot.setPosition(x, y);
    pickupSlot.setSize((int) pickableObject.getShape().getWidth(), (int) pickableObject.getShape().getHeight());
    dragSlot = pickupSlot;
    pickupSlot.setVisible(true);
    gui.clearMouseState();
    gui.handleMouse(x, y, 0, true);
  }

  public void dragStarted(DraggableSlot slot, Event evt) {
    if (slot.getModel() != null) {
      dragSlot = slot;
      dragging(slot, evt);
    }
  }

  @Override
  public void dragging(DraggableSlot slot, Event evt) {
    if (dragSlot != null) {
      ItemSlot dropItemSlot = null;
      Widget w = quickItemInventoryPanel.getWidgetAt(evt.getMouseX(), evt.getMouseY());
      if (w instanceof ItemSlot) {
        dropItemSlot = (ItemSlot) w;
      }
      if (dropItemSlot == null) {
        w = backpackInventoryPanel.getWidgetAt(evt.getMouseX(), evt.getMouseY());
        if (w instanceof ItemSlot) {
          dropItemSlot = (ItemSlot) w;
        }
      }

      if (dropItemSlot != null) {
        setDropSlot(dropItemSlot);
      } else {
        setDropSlot(null);
      }
    }
  }

  @Override
  public void dragStopped(DraggableSlot<Item> slot, Event evt) {
    if (dragSlot != null) {
      dragging(slot, evt);
      if (dropSlot != null) {
        if (dropSlot.canDrop() && dropSlot != dragSlot) {
          Item draggedItem = dragSlot.getModel();
          if (dragSlot instanceof PickupItemSlot) {
            objectKiller.killMovingObject(checkNotNull(pickableObject));
            pickableObject = null;
            pickupSlot.setVisible(false);
          } else {
            int dragItemIndex = dragSlot.getInventoryIndex();
            dragSlot.getInventory().remove(dragItemIndex);
          }
          int dropItemIndex = dropSlot.getInventoryIndex();
          dropSlot.getInventory().add(dropItemIndex, draggedItem);
        }
        setDropSlot(null);
      } else {
        if (dragSlot instanceof PickupItemSlot) {
          pickableObject = null;
          pickupSlot.setVisible(false);
        } else {
          int levelX = evt.getMouseX() + pointOfView.getShiftX();
          int levelY = evt.getMouseY() + pointOfView.getShiftY();
          if (inventoryController.dropItem(slot.getModel(), levelX, levelY)) {
            int dragItemIndex = dragSlot.getInventoryIndex();
            dragSlot.getInventory().remove(dragItemIndex);
          }
        }
      }

      dragSlot = null;
    }
  }

  private void setDropSlot(DraggableSlot slot) {
    if (slot != dropSlot) {
      if (dropSlot != null) {
        dropSlot.resetDropState();
      }
      dropSlot = slot;
      if (dropSlot != null) {
        dropSlot.dropFrom(dragSlot);
      }
    }
  }
}
