package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderItem;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.inventory.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 08.09.12
 */
@Component
public class BackpackInventory extends AbstractInventory<Item> {

  public static final int SIZE = 4;

  protected static final Item emptyItem = new EmptyItem();

  @Autowired
  EventBus eventBus;

  public BackpackInventory() {
    super();
  }

  @PostConstruct
  void init() {
    Assert.executeAndAssert(add(new AxeItem()));
    Assert.executeAndAssert(add(new LadderItem()));
  }

  @Override
  protected Item getEmptyItem() {
    return emptyItem;
  }

  @Override
  public int getSize() {
    return SIZE;
  }

  @Override
  public int getSelectedIndex() {
    return -1;
  }
}
