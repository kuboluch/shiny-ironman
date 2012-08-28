package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.production.AvailableItemsChangeEvent;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionController implements SelectListener<ItemDefinition> {

  @Autowired
  WorkplaceController workplaceController;

  @Autowired
  PlayerController playerController;

  @Autowired
  MainGameUI mainGameUI;

  @Autowired
  ResourceStorageController resourceStorageController;

  @Autowired
  InventoryController inventoryController;

  @Autowired
  EventBus eventBus;

  List<ItemDefinition> items = new ArrayList<ItemDefinition>();

  WorkplaceDefinition activeWorkplace;

  List<ItemDefinition> availableItems = new ArrayList<ItemDefinition>();

  private ItemDefinition selectedItem;

  public ProductionController(List<ItemDefinition> items) {
    this.items = items;
  }

  public List<ItemDefinition> getItemDefinitions() {
    return new ArrayList<ItemDefinition>(items);
  }

  public List<ItemDefinition> getAvailableItemDefinitions() {
    return new ArrayList<ItemDefinition>(availableItems);
  }

  public ItemDefinition getSelectedItem() {
    return selectedItem;
  }

  @Override
  public void onSelect(ItemDefinition selected) {
    selectedItem = selected;
  }

  public void update() {
    int playerX = Sizes.roundToBlockSizeX(playerController.getPlayer().getShape().getCenterX());
    int playerY = Sizes.roundToBlockSizeY(playerController.getPlayer().getShape().getCenterY());
    WorkplaceDefinition newWorkplace = workplaceController.findWorkplace(new Rectangle(playerX, playerY, 1, 1));
    if (activeWorkplace != newWorkplace) {
      activeWorkplace = newWorkplace;
      availableItems.clear();
      if (activeWorkplace != null) {
        for (ItemDefinition item : items) {
          if (!Collections3.collectSubclasses(item.getItemProductionPlaces(), activeWorkplace.getClass()).isEmpty()) {
            availableItems.add(item);
          }
        }
      }
      eventBus.broadcast(new AvailableItemsChangeEvent());
    }
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
