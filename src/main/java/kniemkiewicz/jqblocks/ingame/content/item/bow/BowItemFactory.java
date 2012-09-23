package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.inventory.item.ItemFactory;

/**
 * User: qba
 * Date: 22.08.12
 */
public class BowItemFactory implements ItemFactory<BowItem> {

  @Override
  public BowItem createItem() {
    return new BowItem();
  }
}
