package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import com.google.common.base.Optional;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.ui.inventory.InventoryPanel;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * User: qba
 * Date: 07.09.12
 */
public class ItemSlot extends AbstractDraggableSlot<Item> {

  public static final AnimationState.StateKey STATE_SELECTED = AnimationState.StateKey.get("selected");

  private static final int MARGIN = 5;

  SpringBeanProvider springBeanProvider;

  private int inventoryIndex;
  private Inventory<Item> inventory;
  private Item item;
  private boolean selected = false;

  public ItemSlot(int inventoryIndex, Inventory<Item> inventory, SpringBeanProvider springBeanProvider) {
    this.inventoryIndex = inventoryIndex;
    this.inventory = inventory;
    this.springBeanProvider = springBeanProvider;
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
    return inventory;
  }

  @Override
  public int getInventoryIndex() {
    return inventoryIndex;
  }

  public boolean isSelected() {
    return selected;
  }

  public void select() {
    selected = true;
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_SELECTED, true);
  }

  public void deselect() {
    selected = false;
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_SELECTED, false);
  }

  public boolean canDrop() {
    return isVisible() && (item == null || item.isEmpty());
  }

  @Override
  protected void paintWidget(GUI gui) {
    if (!isDragActive()) {
      TextureImpl.unbind();
      renderModel(gui, getInnerX() + MARGIN, getInnerY() + MARGIN);
      Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
    }
  }

  @Override
  public void renderModel(GUI gui, int x, int y) {
    ItemRenderer<Item> itemRenderer = springBeanProvider.getBean(item.getItemRenderer(), true);
    itemRenderer.renderItem(item, x, y, getInnerWidth() - 2 * MARGIN, false);
  }
}