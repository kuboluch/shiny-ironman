package kniemkiewicz.jqblocks.ingame.ui.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.QuickItemInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class QuickItemInventoryPanel extends InventoryPanel {

  public static final int NUM_SLOTS_X = 10;
  public static final int NUM_SLOTS_Y = 1;

  @Autowired
  QuickItemInventory itemInventory;

  public QuickItemInventoryPanel() {
    super(NUM_SLOTS_X, NUM_SLOTS_Y);
  }

  public Inventory<Item> getInventory() {
    return itemInventory;
  }
}

