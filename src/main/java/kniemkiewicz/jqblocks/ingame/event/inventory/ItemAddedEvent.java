package kniemkiewicz.jqblocks.ingame.event.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;

/**
 * User: qba
 * Date: 08.09.12
 */
public class ItemAddedEvent extends InventoryChangeEvent {
  Item item;

  public ItemAddedEvent(Inventory inventory, Item item) {
    super(inventory);
    this.item = item;
  }

  public Item getItem() {
    return item;
  }
}
