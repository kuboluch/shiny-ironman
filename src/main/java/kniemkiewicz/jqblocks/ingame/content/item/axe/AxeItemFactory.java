package kniemkiewicz.jqblocks.ingame.content.item.axe;

import kniemkiewicz.jqblocks.ingame.inventory.item.ItemFactory;

/**
 * User: qba
 * Date: 22.08.12
 */
public class AxeItemFactory implements ItemFactory<AxeItem> {

  @Override
  public AxeItem createItem() {
    return new AxeItem();
  }
}
