package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderItem;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.inventory.ItemAddedEvent;
import kniemkiewicz.jqblocks.ingame.event.inventory.ItemMovedEvent;
import kniemkiewicz.jqblocks.ingame.event.inventory.ItemRemovedEvent;
import kniemkiewicz.jqblocks.ingame.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.item.Item;
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

  protected final Item emptyItem = new EmptyItem();

  @Autowired
  EventBus eventBus;

  @PostConstruct
  void init() {
    for (int i = 0; i < getSize(); i++) {
      items.add(getEmptyItem());
    }
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
  public boolean add(Item item) {
    eventBus.broadcast(new ItemAddedEvent(this, item));
    return super.add(item);
  }

  @Override
  public boolean add(int index, Item item) {
    eventBus.broadcast(new ItemAddedEvent(this, item));
    return super.add(index, item);
  }

  @Override
  public void removeSelected() {
    eventBus.broadcast(new ItemRemovedEvent(this, getSelectedItem()));
    super.removeSelected();
  }

  @Override
  public void remove(int index) {
    eventBus.broadcast(new ItemRemovedEvent(this, getItems().get(index)));
    super.remove(index);
  }

  public void move(int fromIndex, int toIndex) {
    Assert.assertTrue(!items.get(fromIndex).isEmpty());
    Assert.assertTrue(items.get(toIndex).isEmpty());
    Item item = items.get(fromIndex);
    remove(fromIndex);
    add(toIndex, item);
    eventBus.broadcast(new ItemMovedEvent(this, item, fromIndex, toIndex));
  }
}
