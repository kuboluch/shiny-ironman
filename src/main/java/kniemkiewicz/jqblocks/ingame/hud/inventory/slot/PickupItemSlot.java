package kniemkiewicz.jqblocks.ingame.hud.inventory.slot;

import de.matthiasmann.twl.GUI;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;

/**
 * User: qba
 * Date: 10.09.12
 */
public class PickupItemSlot extends AbstractDraggableSlot<Item> {

  SpringBeanProvider springBeanProvider;
  Item item;

  public PickupItemSlot() {
  }

  public void setSpringBeanProvider(SpringBeanProvider springBeanProvider) {
    this.springBeanProvider = springBeanProvider;
  }

  @Override
  public boolean canDrop(Item item) {
    return false;
  }

  @Override
  public Item getModel() {
    return item;
  }

  public void setModel(Item item) {
    this.item = item;
  }

  @Override
  public Inventory<Item> getInventory() {
    return null;
  }

  @Override
  public int getInventoryIndex() {
    return -1;
  }

  @Override
  public void renderModel(GUI gui, int x, int y) {
    if (item != null) {
      ItemRenderer<Item> itemRenderer = springBeanProvider.getBean(item.getItemRenderer(), true);
      itemRenderer.renderItem(item, x, y, getInnerWidth(), false);
    }
  }
}
