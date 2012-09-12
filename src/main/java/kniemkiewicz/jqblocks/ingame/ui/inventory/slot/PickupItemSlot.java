package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import com.google.common.base.Optional;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
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
  public boolean canDrop() {
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
    ItemRenderer<Item> itemRenderer = springBeanProvider.getBean(item.getItemRenderer(), true);
    itemRenderer.renderItem(item, x, y, getInnerWidth(), false);
  }

  @Override
  protected boolean handleEvent(Event evt) {
    return super.handleEvent(evt);
  }
}
