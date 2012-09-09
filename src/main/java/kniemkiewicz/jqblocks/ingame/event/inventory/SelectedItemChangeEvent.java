package kniemkiewicz.jqblocks.ingame.event.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;

/**
 * User: qba
 * Date: 29.08.12
 */
public class SelectedItemChangeEvent extends InventoryChangeEvent {
  int oldSelectedIndex;
  int newSelectedIndex;

  public SelectedItemChangeEvent(Inventory inventory, int oldSelectedIndex, int newSelectedIndex) {
    super(inventory);
    this.oldSelectedIndex = oldSelectedIndex;
    this.newSelectedIndex = newSelectedIndex;
  }

  public int getOldSelectedIndex() {
    return oldSelectedIndex;
  }

  public int getNewSelectedIndex() {
    return newSelectedIndex;
  }
}
