package kniemkiewicz.jqblocks.ingame.hud.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.QuickItemInventory;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import kniemkiewicz.jqblocks.ingame.hud.inventory.slot.ItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class QuickItemInventoryPanel extends AbstractInventoryPanel<Item> implements ItemValidator {

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

  @Override
  public ItemValidator getItemValidator() {
    return this;
  }

  @Override
  public boolean isValid(Item item) {
    return !(item instanceof ResourceItem);
  }
}

