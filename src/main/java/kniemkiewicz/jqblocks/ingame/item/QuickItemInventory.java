package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.content.block.dirt.DirtBlockItem;
import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.content.item.bow.BowItem;
import kniemkiewicz.jqblocks.ingame.content.item.pickaxe.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderItem;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.inventory.AbstractInventory;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
@Component
public class QuickItemInventory extends AbstractInventory<Item> {

  @Autowired
  EventBus eventBus;

  protected static final Item emptyItem = new EmptyItem();

  public QuickItemInventory() {
    super();
  }

  @PostConstruct
  void init() {
    Assert.executeAndAssert(add(new BowItem()));
    Assert.executeAndAssert(add(new PickaxeItem()));
    Assert.executeAndAssert(add(new AxeItem()));
    Assert.executeAndAssert(add(new LadderItem()));
    Assert.executeAndAssert(add(new DirtBlockItem()));
    Assert.executeAndAssert(add(new PickaxeItem(1000000)));
  }

  @Override
  protected Item getEmptyItem() {
    return emptyItem;
  }
}

