package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.inventory.item.ItemFactory;

/**
 * User: qba
 * Date: 22.08.12
 */
public class DirtBlockItemFactory implements ItemFactory<DirtBlockItem> {

  @Override
  public DirtBlockItem createItem() {
    return new DirtBlockItem();
  }
}
