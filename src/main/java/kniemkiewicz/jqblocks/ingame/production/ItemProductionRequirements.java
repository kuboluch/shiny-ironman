package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;

import java.util.List;

/**
 * User: qba
 * Date: 25.08.12
 */
public interface ItemProductionRequirements {
  List<ResourceRequirement> getResourceRequirements();
  List<Item> getItemRequirements();
}
