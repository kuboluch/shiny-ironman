package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.item.ItemFactory;

/**
 * User: qba
 * Date: 21.08.12
 */
public class LadderItemFactory implements ItemFactory<LadderItem> {

  @Override
  public LadderItem createItem() {
    return new LadderItem();
  }
}
