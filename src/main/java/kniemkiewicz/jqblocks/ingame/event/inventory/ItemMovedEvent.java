package kniemkiewicz.jqblocks.ingame.event.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;

/**
 * User: qba
 * Date: 08.09.12
 */
public class ItemMovedEvent extends InventoryChangeEvent {
  Item item;
  int fromIndex;
  int toIndex;

  public ItemMovedEvent(Inventory inventory, Item item, int fromIndex, int toIndex) {
    super(inventory);
    this.item = item;
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
  }

  public Item getItem() {
    return item;
  }

  public int getFromIndex() {
    return fromIndex;
  }

  public int getToIndex() {
    return toIndex;
  }
}
