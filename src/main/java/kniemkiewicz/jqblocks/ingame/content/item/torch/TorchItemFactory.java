package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.item.ItemFactory;

/**
 * User: qba
 * Date: 29.08.12
 */
public class TorchItemFactory implements ItemFactory<TorchItem> {

  @Override
  public TorchItem createItem() {
    return getItem();
  }

  public static TorchItem getItem() {
    return new TorchItem();
  }
}
