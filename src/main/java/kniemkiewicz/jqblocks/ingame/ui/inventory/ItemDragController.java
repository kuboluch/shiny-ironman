package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class ItemDragController implements ItemSlot.DragListener {

  @Autowired
  InventoryController inventoryController;

  @Autowired
  QuickItemInventoryPanel quickItemInventoryPanel;

  @Autowired
  BackpackInventoryPanel backpackInventoryPanel;

  @Autowired
  PointOfView pointOfView;

  private ItemSlot dragSlot;
  private ItemSlot dropSlot;

  public void dragStarted(ItemSlot slot, Event evt) {
    if (slot.getItem() != null) {
      dragSlot = slot;
      dragging(slot, evt);
    }
  }

  @Override
  public void dragging(ItemSlot itemSlot, Event evt) {
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
  public void dragStopped(ItemSlot itemSlot, Event evt) {
    if (dragSlot != null) {
      dragging(itemSlot, evt);
      if (dropSlot != null) {
        if (dropSlot.canDrop() && dropSlot != dragSlot) {
          int dragItemIndex = dragSlot.getPanel().getSlotIndex(dragSlot);
          int dropItemIndex = dropSlot.getPanel().getSlotIndex(dropSlot);
          Item draggedItem = dragSlot.getItem();
          dragSlot.getPanel().getInventory().remove(dragItemIndex);
          dropSlot.getPanel().getInventory().add(dropItemIndex, draggedItem);
        }
        setDropSlot(null);
      } else {
        int levelX = evt.getMouseX() + pointOfView.getShiftX();
        int levelY = evt.getMouseY() + pointOfView.getShiftY();
        if (inventoryController.dropItem(itemSlot.getItem(), levelX, levelY)) {
          int dragItemIndex = dragSlot.getPanel().getSlotIndex(dragSlot);
          dragSlot.getPanel().getInventory().remove(dragItemIndex);
        }
      }

      dragSlot = null;
    }
  }

  private void setDropSlot(ItemSlot itemSlot) {
    if (itemSlot != dropSlot) {
      if (dropSlot != null) {
        dropSlot.setDropState(false, false);
      }
      dropSlot = itemSlot;
      if (dropSlot != null) {
        dropSlot.setDropState(true, dropSlot == dragSlot || dropSlot.canDrop());
      }
    }
  }
}
