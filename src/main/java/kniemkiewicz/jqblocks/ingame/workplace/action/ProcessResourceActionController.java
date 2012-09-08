package kniemkiewicz.jqblocks.ingame.workplace.action;

import kniemkiewicz.jqblocks.ingame.action.Interactive;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.resource.inventory.ResourceInventory;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
public class ProcessResourceActionController implements Interactive {

  private static final int PROCESS_DURATION = 1000;

  @Autowired
  ResourceInventory inventory;

  @Autowired
  ResourceStorageController resourceStorageController;

  ResourceType resourceType;

  public ProcessResourceActionController(ResourceType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public boolean canInteract() {
    for (ResourceItem item : inventory.getItems()) {
      if (!item.isEmpty() && resourceType.equals(item.getResource().getType())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void interact() {
    int itemIndex = -1;
    List<ResourceItem> items = inventory.getItems();
    for (ResourceItem item : items) {
      if (!item.isEmpty() && resourceType.equals(item.getResource().getType())) {
        itemIndex = items.indexOf(item);
      }
    }

    if (itemIndex >= 0) {
      Resource resource = items.get(itemIndex).getResource();
      resourceStorageController.fill(resource);
      inventory.remove(itemIndex);
    }
  }

  @Override
  public int getActionDuration() {
    return PROCESS_DURATION;
  }
}
