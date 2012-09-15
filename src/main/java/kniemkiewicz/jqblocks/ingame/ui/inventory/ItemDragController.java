package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.ui.Initializable;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.DragListener;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.DraggableSlot;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.ItemSlot;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.PickupItemSlot;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class ItemDragController implements DragListener<Item>, Initializable, EventListener {

  @Autowired
  MainGameUI mainGameUI;

  @Autowired
  InventoryController inventoryController;

  @Autowired
  QuickItemInventoryPanel quickItemInventoryPanel;

  @Autowired
  ResourceInventoryPanel resourceInventoryPanel;

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

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) ScreenMovedEvent.class);
  }

  @Override
  public void listen(List<kniemkiewicz.jqblocks.ingame.event.Event> events) {
    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      dragInterrupted();
    }
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
      ItemSlot dropItemSlot = getItemSlotAt(quickItemInventoryPanel, evt.getMouseX(), evt.getMouseY());
      if (dropItemSlot == null) {
         dropItemSlot = getItemSlotAt(resourceInventoryPanel, evt.getMouseX(), evt.getMouseY());
      }
      if (dropItemSlot == null) {
         dropItemSlot = getItemSlotAt(backpackInventoryPanel, evt.getMouseX(), evt.getMouseY());
      }

      if (dropItemSlot != null) {
        setDropSlot(dropItemSlot);
      } else {
        setDropSlot(null);
      }
    }
  }

  private ItemSlot getItemSlotAt(AbstractInventoryPanel inventory, int x, int y) {
    ItemSlot itemSlot = null;
    Widget w = inventory.getWidgetAt(x, y);
    if (w instanceof ItemSlot) {
      itemSlot = (ItemSlot) w;
    }
    return itemSlot;
  }

  @Override
  public void dragStopped(DraggableSlot<Item> slot, Event evt) {
    if (dragSlot != null) {
      dragging(slot, evt);
      if (dropSlot != null) {
        if (dropSlot.canDrop(dragSlot.getModel()) && dropSlot != dragSlot) {
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

  public void dragInterrupted() {
    if (dragSlot instanceof PickupItemSlot) {
      pickableObject = null;
      pickupSlot.setVisible(false);
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
