package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.tests.xml.Inventory;

/**
 * User: qba
 * Date: 07.09.12
 */
public class ItemSlot extends Widget {

  public static final AnimationState.StateKey STATE_SELECTED = AnimationState.StateKey.get("selected");
  public static final AnimationState.StateKey STATE_DRAG_ACTIVE = AnimationState.StateKey.get("dragActive");
  public static final AnimationState.StateKey STATE_DROP_OK = AnimationState.StateKey.get("dropOk");
  public static final AnimationState.StateKey STATE_DROP_BLOCKED = AnimationState.StateKey.get("dropBlocked");

  private static final int MARGIN = 5;

  public interface DragListener {
    public void dragStarted(ItemSlot slot, Event evt);

    public void dragging(ItemSlot slot, Event evt);

    public void dragStopped(ItemSlot slot, Event evt);
  }

  SpringBeanProvider springBeanProvider;

  InventoryPanel panel;

  private Item item;
  private DragListener listener;
  private boolean dragActive;
  private boolean selected = false;

  public ItemSlot(InventoryPanel panel, SpringBeanProvider springBeanProvider) {
    this.panel = panel;
    this.springBeanProvider = springBeanProvider;
  }

  public InventoryPanel getPanel() {
    return panel;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
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
    return item == null || item.isEmpty();
  }

  public void setListener(DragListener listener) {
    this.listener = listener;
  }

  public void setDropState(boolean drop, boolean ok) {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_DROP_OK, drop && ok);
    as.setAnimationState(STATE_DROP_BLOCKED, drop && !ok);
  }

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEventNoWheel()) {
      if (dragActive) {
        if (evt.isMouseDragEnd()) {
          if (listener != null) {
            listener.dragStopped(this, evt);
          }
          dragActive = false;
          getAnimationState().setAnimationState(STATE_DRAG_ACTIVE, false);
        } else if (listener != null) {
          listener.dragging(this, evt);
        }
      } else if (evt.isMouseDragEvent()) {
        dragActive = true;
        getAnimationState().setAnimationState(STATE_DRAG_ACTIVE, true);
        if (listener != null) {
          listener.dragStarted(this, evt);
        }
      }
      return true;
    }
    return super.handleEvent(evt);
  }

  @Override
  protected void paintWidget(GUI gui) {
    if (!dragActive) {
      TextureImpl.unbind();
      ItemRenderer<Item> itemRenderer = springBeanProvider.getBean(item.getItemRenderer(), true);
      itemRenderer.renderItem(item, getInnerX() + MARGIN, getInnerY() + MARGIN, getInnerWidth() - 2 * MARGIN, false);
      Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
    }
  }

  @Override
  protected void paintDragOverlay(GUI gui, int mouseX, int mouseY, int modifier) {
    TextureImpl.unbind();
    ItemRenderer<Item> itemRenderer = springBeanProvider.getBean(item.getItemRenderer(), true);
    itemRenderer.renderItem(item, mouseX - getInnerWidth() / 2, mouseY - getInnerHeight() / 2, getInnerWidth(), false);
    Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
  }
}