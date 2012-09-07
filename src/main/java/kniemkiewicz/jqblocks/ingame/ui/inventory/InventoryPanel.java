package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderItem;
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

  private ItemSlot[] slot;

  private int slotSpacing;

  private ItemSlot dragSlot;
  private ItemSlot dropSlot;

  public InventoryPanel() {
  }

  @PostConstruct
  public void init() {
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

    slot[0].changeItem(new AxeItem());
    slot[2].changeItem(new LadderItem());
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

  void dragging(ItemSlot slot, Event evt) {
    if (dragSlot != null) {
      Widget w = getWidgetAt(evt.getMouseX(), evt.getMouseY());
      if (w instanceof ItemSlot) {
        setDropSlot((ItemSlot) w);
      } else {
        setDropSlot(null);
      }
    }
  }

  void dragStopped(ItemSlot slot, Event evt) {
    if (dragSlot != null) {
      dragging(slot, evt);
      if (dropSlot != null && dropSlot.canDrop() && dropSlot != dragSlot) {
        dropSlot.changeItem(dragSlot.getItem());
        dragSlot.changeItem(null);
      }
      setDropSlot(null);
      dragSlot = null;
    }
  }

  private void setDropSlot(ItemSlot slot) {
    if (slot != dropSlot) {
      if (dropSlot != null) {
        dropSlot.setDropState(false, false);
      }
      dropSlot = slot;
      if (dropSlot != null) {
        dropSlot.setDropState(true, dropSlot == dragSlot || dropSlot.canDrop());
      }
    }
  }
}
