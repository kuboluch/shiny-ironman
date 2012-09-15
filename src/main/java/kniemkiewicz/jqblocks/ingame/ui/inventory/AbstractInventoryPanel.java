package kniemkiewicz.jqblocks.ingame.ui.inventory;

import com.google.common.base.Objects;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.ItemSlot;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.ItemValidator;
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
public abstract class AbstractInventoryPanel<T extends Item> extends Widget {

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Autowired
  ItemDragController itemDragController;

  int xSlotsNumber;
  int ySlotsNumber;

  ItemSlot[] slot;

  int slotSpacing;

  public AbstractInventoryPanel(int xSlotsNumber, int ySlotsNumber) {
    this.xSlotsNumber = xSlotsNumber;
    this.ySlotsNumber = ySlotsNumber;
  }

  @PostConstruct
  public void init() {
    Assert.assertTrue(getInventory() != null);
    Assert.assertTrue(getInventory().getSize() == xSlotsNumber * ySlotsNumber);

    this.slot = new ItemSlot[xSlotsNumber * ySlotsNumber];

    for (int i = 0; i < slot.length; i++) {
      slot[i] = new ItemSlot(getInventory(), i, springBeanProvider);
      slot[i].setListener(itemDragController);
      if (getItemValidator() != null) {
        slot[i].setItemValidator(getItemValidator());
      }
      add(slot[i]);
    }

    update();
  }

  public abstract Inventory<T> getInventory();

  public abstract ItemValidator getItemValidator();

  public void update() {
    deselectAll();
    for (int i = 0; i < slot.length; i++) {
      Item item = getInventory().getItems().get(i);
      if (!Objects.equal(slot[i].getModel(), item)) {
        slot[i].setModel(item);
      }
      if (i == getInventory().getSelectedIndex()) {
        slot[i].select();
      }
    }
  }

  private void deselectAll() {
    for (int i = 0; i < slot.length; i++) {
      slot[i].deselect();
    }
  }

  @Override
  public int getPreferredInnerWidth() {
    return (slot[0].getPreferredWidth() + slotSpacing) * xSlotsNumber - slotSpacing;
  }

  @Override
  public int getPreferredInnerHeight() {
    return (slot[0].getPreferredHeight() + slotSpacing) * ySlotsNumber - slotSpacing;
  }

  public int getXSlotsNumber() {
    return xSlotsNumber;
  }

  public int getYSlotsNumber() {
    return ySlotsNumber;
  }

  public int getSlotSpacing() {
    return slotSpacing;
  }

  @Override
  protected void layout() {
    int slotWidth = slot[0].getPreferredWidth();
    int slotHeight = slot[0].getPreferredHeight();

    for (int row = 0, y = getInnerY(), i = 0; row < ySlotsNumber; row++) {
      for (int col = 0, x = getInnerX(); col < xSlotsNumber; col++, i++) {
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

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    for (int i = 0, n = getNumChildren(); i < n; i++) {
      Widget child = getChild(i);
      child.setVisible(visible);
    }
  }
}
