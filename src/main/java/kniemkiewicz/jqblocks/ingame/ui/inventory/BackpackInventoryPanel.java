package kniemkiewicz.jqblocks.ingame.ui.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.BackpackInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.ingame.ui.inventory.slot.ItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class BackpackInventoryPanel extends AbstractInventoryPanel<Item> implements ItemValidator {

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

  @Override
  public ItemValidator getItemValidator() {
    return this;
  }

  @Override
  public boolean isValid(Item item) {
    return !(item instanceof ResourceItem);
  }
}
