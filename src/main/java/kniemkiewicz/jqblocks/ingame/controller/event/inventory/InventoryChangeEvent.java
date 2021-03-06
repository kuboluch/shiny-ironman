package kniemkiewicz.jqblocks.ingame.controller.event.inventory;

import kniemkiewicz.jqblocks.ingame.controller.event.AbstractEvent;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;

/**
 * User: qba
 * Date: 08.09.12
 */
public class InventoryChangeEvent extends AbstractEvent {
  Inventory inventory;

  public InventoryChangeEvent(Inventory inventory) {
    this.inventory = inventory;
  }

  public Inventory getInventory() {
    return inventory;
  }
}
