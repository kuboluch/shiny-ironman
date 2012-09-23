package kniemkiewicz.jqblocks.ingame.resource.inventory;

import kniemkiewicz.jqblocks.ingame.inventory.AbstractInventory;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.item.ResourceItem;
import org.springframework.stereotype.Component;

@Component
public class ResourceInventory extends AbstractInventory<ResourceItem> implements Inventory<ResourceItem> {

  public static final int SIZE = 2;

  private static class ResourceEmptyItem extends EmptyItem implements ResourceItem {
    @Override
    public Resource getResource() {
      return null;
    }
  }

  protected static final ResourceItem emptyItem = new ResourceEmptyItem();

  public ResourceInventory() {
    super();
  }

  @Override
  protected ResourceItem getEmptyItem() {
    return emptyItem;
  }

  @Override
  public int getSize() {
    return SIZE;
  }
}
