package kniemkiewicz.jqblocks.ingame.ui.produce;

import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.resource.ResourceStorageChangeEvent;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.production.ResourceRequirement;
import kniemkiewicz.jqblocks.ingame.ui.widget.Panel;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;
import kniemkiewicz.jqblocks.util.Collections3;

import java.util.Arrays;
import java.util.List;

/**
 * User: qba
 * Date: 26.08.12
 */
public class ProductionRequirementsPanel extends Panel<ResourceRequirementPanelItem>
    implements SelectListener<ItemDefinition>, EventListener {
  static final int ITEM_WIDTH = 40;
  static final int ITEM_HEIGHT = 40;

  ItemDefinition selectedItem;

  public ProductionRequirementsPanel() {
    super();
  }

  @Override
  public int getEmptyWidth() {
    return ITEM_WIDTH;
  }

  @Override
  public int getEmptyHeight() {
    return ITEM_HEIGHT;
  }

  @Override
  public void onSelect(ItemDefinition selected) {
    this.selectedItem = selected;
    removeAllItems();
    for (ResourceRequirement resourceRequirement : selectedItem.getResourceRequirements()) {
      ResourceRequirementPanelItem panelItem = new ResourceRequirementPanelItem(resourceRequirement, ITEM_WIDTH, ITEM_HEIGHT);
      addItem(panelItem);
      panelItem.updateState();
    }
  }

  @Override
  public void listen(List<Event> events) {
    List<ResourceStorageChangeEvent> resourceStorageChangeEvents = Collections3.collect(events, ResourceStorageChangeEvent.class);
    if (!resourceStorageChangeEvents.isEmpty()) {
      for (ResourceRequirementPanelItem panelItem : panelItems) {
        panelItem.updateState();
      }
    }
  }

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) ResourceStorageChangeEvent.class);
  }
}
