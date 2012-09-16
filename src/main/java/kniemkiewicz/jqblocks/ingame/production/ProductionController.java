package kniemkiewicz.jqblocks.ingame.production;

import com.google.common.collect.ImmutableList;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.production.AvailableItemsChangeEvent;
import kniemkiewicz.jqblocks.ingame.event.production.ProductionCompleteEvent;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionController implements SelectListener<ItemDefinition>, EventListener {

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
  ProductionAssignmentController productionAssignmentController;

  @Autowired
  EventBus eventBus;

  List<ItemDefinition> items = new ArrayList<ItemDefinition>();

  WorkplaceDefinition activeWorkplace;

  List<ItemDefinition> availableItems = new ArrayList<ItemDefinition>();

  private ItemDefinition selectedItem;

  public ProductionController(List<ItemDefinition> items) {
    this.items = ImmutableList.copyOf(items);
  }

  @PostConstruct
  public void init() {
    updateAvailableItems();
  }

  public List<ItemDefinition> getAvailableItemDefinitions() {
    return availableItems;
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
      updateAvailableItems();
    }
  }

  private void updateAvailableItems() {
    availableItems.clear();
    for (ItemDefinition item : items) {
      if (item.isGloballyAvailable() || (activeWorkplace != null && item.canBeProducedIn(activeWorkplace))) {
        availableItems.add(item);
      }
    }
    eventBus.broadcast(new AvailableItemsChangeEvent());
    if (!availableItems.contains(selectedItem)) {
      selectedItem = null;
    }
  }

  public boolean assignProduction() {
    List<ResourceRequirement> resourceRequirements = selectedItem.getResourceRequirements();
    if (hasEnoughResources(resourceRequirements)) {
      return false;
    }

    // TODO item requiremnts check

    if (selectedItem.isGloballyAvailable()) {
      productionAssignmentController.assign(playerController.getPlayer(), ProductionAssignment.on(selectedItem));
      consumeResources(resourceRequirements);
      // TODO item consume
      return true;
    }

    int playerX = Sizes.roundToBlockSizeX(playerController.getPlayer().getShape().getCenterX());
    int playerY = Sizes.roundToBlockSizeY(playerController.getPlayer().getShape().getCenterY());
    WorkplaceBackgroundElement wbe = workplaceController.findWorkplaceBackgroundElement(new Rectangle(playerX, playerY, 1, 1));
    if (selectedItem.canBeProducedIn(wbe.getWorkplace())) {
      productionAssignmentController.assign(wbe, ProductionAssignment.on(selectedItem));
      consumeResources(resourceRequirements);
      // TODO item consume
      return true;
    }

    return false;
  }

  private void consumeResources(List<ResourceRequirement> resourceRequirements) {
    for (ResourceRequirement resourceRequirement : resourceRequirements) {
      resourceStorageController.empty(resourceRequirement.getResource().getType(), resourceRequirement.getResource().getAmount());
    }
  }

  private boolean hasEnoughResources(List<ResourceRequirement> resourceRequirements) {
    for (ResourceRequirement resourceRequirement : resourceRequirements) {
      if (!resourceStorageController.hasEnoughResource(resourceRequirement.getResource())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void listen(List<Event> events) {
    List<ProductionCompleteEvent> productionCompleteEvents = Collections3.collect(events, ProductionCompleteEvent.class);
    if (!productionCompleteEvents.isEmpty()) {
      for (ProductionCompleteEvent e : productionCompleteEvents) {
        handleProductionCompleteEvent(e);
      }
    }
  }

  private void handleProductionCompleteEvent(ProductionCompleteEvent e) {
    Item item = e.getAssignment().getItem().createItem();
    if (!inventoryController.addItem(item)) {
      int playerX = (int) playerController.getPlayer().getShape().getCenterX();
      int playerY = (int) playerController.getPlayer().getShape().getCenterY();
      inventoryController.dropItem(item, playerX, playerY);
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) ProductionCompleteEvent.class);
  }
}
