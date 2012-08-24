package kniemkiewicz.jqblocks.ingame.content.item.pickaxe;

import kniemkiewicz.jqblocks.ingame.item.ItemFactory;

/**
 * User: qba
 * Date: 22.08.12
 */
public class PickaxeItemFactory implements ItemFactory<PickaxeItem> {

  @Override
  public PickaxeItem createItem() {
    return new PickaxeItem();
  }
}
