package kniemkiewicz.jqblocks.ingame.hud.inventory.slot;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;
import kniemkiewicz.jqblocks.ingame.inventory.InventoryController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * User: qba
 * Date: 07.09.12
 */
public class ItemSlot<T extends Item> extends AbstractDraggableSlot<T> {

  public static final AnimationState.StateKey STATE_SELECTED = AnimationState.StateKey.get("selected");

  private static final int MARGIN = 5;

  SpringBeanProvider springBeanProvider;

  private int inventoryIndex;
  private Inventory<T> inventory;
  private T item;
  private boolean selected = false;
  private ItemValidator itemValidator;

  public ItemSlot(Inventory<T> inventory, int inventoryIndex, SpringBeanProvider springBeanProvider) {
    this.inventory = inventory;
    this.inventoryIndex = inventoryIndex;
    this.springBeanProvider = springBeanProvider;
  }

  @Override
  public T getModel() {
    return item;
  }

  public void setModel(T item) {
    this.item = item;
  }

  public void setItemValidator(ItemValidator itemValidator) {
    this.itemValidator = itemValidator;
  }

  @Override
  public Inventory<T> getInventory() {
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

  public boolean canDrop(T item) {
    boolean canDrop = isVisible() && (this.item == null || this.item.isEmpty());
    if (itemValidator != null) {
      canDrop = canDrop && itemValidator.isValid(item);
    }
    return canDrop;
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

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEventNoWheel()) {
      if (evt.getType().equals(Event.Type.MOUSE_BTNDOWN)) {
        if (evt.getMouseButton() == Event.MOUSE_LBUTTON) {
          if (!isSelected()) {
            InventoryController inventoryController = springBeanProvider.getBean(InventoryController.class, true);
            inventoryController.setInventorySelectedIndex(inventoryIndex);
          }
        }
        if (evt.getMouseButton() == Event.MOUSE_RBUTTON) {
          InventoryController inventoryController = springBeanProvider.getBean(InventoryController.class, true);
          PlayerController playerController = springBeanProvider.getBean(PlayerController.class, true);
          int x = (int) playerController.getPlayer().getXYMovement().getX();
          int y = (int) playerController.getPlayer().getXYMovement().getY();
          if (inventoryController.dropItem(getModel(), x, y)) {
            inventory.remove(inventoryIndex);
          }
        }
      }
      return super.handleEvent(evt);
    }
    return super.handleEvent(evt);
  }
}