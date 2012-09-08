package kniemkiewicz.jqblocks.ingame.ui.inventory;

import com.google.common.base.Objects;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.inventory.BackpackInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 07.09.12
 */
@Component
public class InventoryPanel extends Widget {

  public static final int NUM_SLOTS_X = 2;
  public static final int NUM_SLOTS_Y = 2;

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  InventoryController inventoryController;

  @Autowired
  BackpackInventory backpackInventory;

  private ItemSlot[] slot;

  private int slotSpacing;

  private ItemSlot dragSlot;
  private ItemSlot dropSlot;

  public InventoryPanel() {
  }

  @PostConstruct
  public void init() {
    Assert.assertTrue(backpackInventory.getSize() == NUM_SLOTS_X * NUM_SLOTS_Y);

    this.slot = new ItemSlot[NUM_SLOTS_X * NUM_SLOTS_Y];

    ItemSlot.DragListener listener = new ItemSlot.DragListener() {
      public void dragStarted(ItemSlot slot, Event evt) {
        InventoryPanel.this.dragStarted(slot, evt);
      }

      public void dragging(ItemSlot slot, Event evt) {
        InventoryPanel.this.dragging(slot, evt);
      }

      public void dragStopped(ItemSlot slot, Event evt) {
        InventoryPanel.this.dragStopped(slot, evt);
      }
    };

    for (int i = 0; i < slot.length; i++) {
      slot[i] = new ItemSlot(springBeanProvider);
      slot[i].setListener(listener);
      add(slot[i]);
    }

    update();
  }

  public Inventory<Item> getModel() {
    return backpackInventory;
  }

  public void update() {
    for (int i = 0; i < slot.length; i++) {
      Item item = backpackInventory.getItems().get(i);
      if (!Objects.equal(slot[i].getItem(), item)) {
        slot[i].setItem(item);
      }
    }
  }

  @Override
  public int getPreferredInnerWidth() {
    return (slot[0].getPreferredWidth() + slotSpacing) * NUM_SLOTS_X - slotSpacing;
  }

  @Override
  public int getPreferredInnerHeight() {
    return (slot[0].getPreferredHeight() + slotSpacing) * NUM_SLOTS_Y - slotSpacing;
  }

  @Override
  protected void layout() {
    int slotWidth = slot[0].getPreferredWidth();
    int slotHeight = slot[0].getPreferredHeight();

    for (int row = 0, y = getInnerY(), i = 0; row < NUM_SLOTS_Y; row++) {
      for (int col = 0, x = getInnerX(); col < NUM_SLOTS_X; col++, i++) {
        slot[i].adjustSize();
        slot[i].setPosition(x, y);
        x += slotWidth + slotSpacing;
      }
      y += slotHeight + slotSpacing;
    }
  }

  @Override
  protected void applyTheme(ThemeInfo themeInfo) {
    super.applyTheme(themeInfo);
    slotSpacing = themeInfo.getParameter("slotSpacing", 5);
  }

  void dragStarted(ItemSlot slot, Event evt) {
    if (slot.getItem() != null) {
      dragSlot = slot;
      dragging(slot, evt);
    }
  }

  void dragging(ItemSlot itemSlot, Event evt) {
    if (dragSlot != null) {
      Widget w = getWidgetAt(evt.getMouseX(), evt.getMouseY());
      if (w instanceof ItemSlot) {
        setDropSlot((ItemSlot) w);
      } else {
        setDropSlot(null);
      }
    }
  }

  void dragStopped(ItemSlot itemSlot, Event evt) {
    if (dragSlot != null) {
      dragging(itemSlot, evt);
      if (dropSlot != null) {
        if (dropSlot.canDrop() && dropSlot != dragSlot) {
          int dragItemIndex = getSlotIndex(dragSlot);
          int dropItemIndex = getSlotIndex(dropSlot);
          backpackInventory.move(dragItemIndex, dropItemIndex);
        }
        setDropSlot(null);
      } else {
        int levelX = evt.getMouseX() + pointOfView.getShiftX();
        int levelY = evt.getMouseY() + pointOfView.getShiftY();
        if (inventoryController.dropItem(itemSlot.getItem(), levelX, levelY)) {
          int dragItemIndex = getSlotIndex(dragSlot);
          backpackInventory.remove(dragItemIndex);
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

  private int getSlotIndex(ItemSlot itemSlot) {
    for (int i = 0; i < slot.length; i++) {
      if (Objects.equal(slot[i], itemSlot)) {
        return i;
      }
    }
    return -1;
  }
}
