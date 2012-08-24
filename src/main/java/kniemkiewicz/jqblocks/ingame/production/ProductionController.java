package kniemkiewicz.jqblocks.ingame.production;

import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionController {

  @Autowired
  MainGameUI mainGameUI;

  List<ItemDefinition> items = new ArrayList<ItemDefinition>();

  private ItemDefinition selectedItem;

  public ProductionController(List<ItemDefinition> items) {
    this.items = items;
  }

  public List<ItemDefinition> getItems() {
    return new ArrayList<ItemDefinition>(items);
  }

  public void changeSelected(ItemDefinition selected) {
    selectedItem = selected;
  }

  public ItemDefinition getSelectedItem() {
    return selectedItem;
  }
}
