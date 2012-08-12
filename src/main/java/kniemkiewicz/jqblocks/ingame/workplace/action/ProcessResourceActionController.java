package kniemkiewicz.jqblocks.ingame.workplace.action;

import kniemkiewicz.jqblocks.ingame.action.Interactive;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventory;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: qba
 * Date: 12.08.12
 */
public class ProcessResourceActionController implements Interactive {

  @Autowired
  ResourceInventory inventory;

  @Autowired
  ResourceStorageController resourceStorageController;

  String resourceType;

  public ProcessResourceActionController(String resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public void interact() {
    ResourceItem resourceItem = inventory.getSelectedItem();
    if (resourceItem != null) {
      Resource resource = resourceItem.getResource();
      resourceStorageController.fill(resource);
      inventory.removeSelectedItem();
    }
  }
}
