package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.util.*;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionController implements SelectListener<ItemDefinition> {

  @Autowired
  MainGameUI mainGameUI;

  @Autowired
  ResourceStorageController resourceStorageController;

  @Autowired
  InventoryController inventoryController;

  List<ItemDefinition> items = new ArrayList<ItemDefinition>();

  private ItemDefinition selectedItem;

  public ProductionController(List<ItemDefinition> items) {
    this.items = items;
  }

  public List<ItemDefinition> getItemDefinitions() {
    return new ArrayList<ItemDefinition>(items);
  }

  public ItemDefinition getSelectedItem() {
    return selectedItem;
  }

  @Override
  public void onSelect(ItemDefinition selected) {
    selectedItem = selected;
  }

  public boolean produce() {
    List<ResourceRequirement> resourceRequirements = selectedItem.getResourceRequirements();
    for (ResourceRequirement resourceRequirement : resourceRequirements) {
      if (!resourceStorageController.hasEnoughResource(resourceRequirement.getResource())) {
        return false;
      }
    }

    // TODO item requiremnts check

    for (ResourceRequirement resourceRequirement : resourceRequirements) {
      resourceStorageController.empty(resourceRequirement.getResource().getType(), resourceRequirement.getResource().getAmount());
    }

    // TODO item destroy

    inventoryController.addItem(selectedItem.createItem());
    return true;
  }
}
