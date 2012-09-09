package kniemkiewicz.jqblocks.ingame.ui.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.BackpackInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class BackpackInventoryPanel extends InventoryPanel {

  public static final int NUM_SLOTS_X = 2;
  public static final int NUM_SLOTS_Y = 2;

  @Autowired
  BackpackInventory backpackInventory;

  public BackpackInventoryPanel() {
    super(NUM_SLOTS_X, NUM_SLOTS_Y);
  }

  @Override
  public Inventory<Item> getInventory() {
    return backpackInventory;
  }
}
