package kniemkiewicz.jqblocks.ingame.controller.event.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;

/**
 * User: qba
 * Date: 08.09.12
 */
public class ItemRemovedEvent extends InventoryChangeEvent {
  Item item;

  public ItemRemovedEvent(Inventory inventory, Item item) {
    super(inventory);
    this.item = item;
  }

  public Item getItem() {
    return item;
  }
}
