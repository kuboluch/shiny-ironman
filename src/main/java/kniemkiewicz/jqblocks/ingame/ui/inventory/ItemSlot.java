package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.TextureImpl;

/**
 * User: qba
 * Date: 07.09.12
 */
public class ItemSlot extends Widget {

  public static final AnimationState.StateKey STATE_DRAG_ACTIVE = AnimationState.StateKey.get("dragActive");
  public static final AnimationState.StateKey STATE_DROP_OK = AnimationState.StateKey.get("dropOk");
  public static final AnimationState.StateKey STATE_DROP_BLOCKED = AnimationState.StateKey.get("dropBlocked");

  public interface DragListener {
    public void dragStarted(ItemSlot slot, Event evt);

    public void dragging(ItemSlot slot, Event evt);

    public void dragStopped(ItemSlot slot, Event evt);
  }

  SpringBeanProvider springBeanProvider;

  private Item item;
  private Image icon;
  private DragListener listener;
  private boolean dragActive;

  public ItemSlot(SpringBeanProvider springBeanProvider) {
    this.springBeanProvider = springBeanProvider;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
    this.icon = null;
    if (this.item != null) {
      this.icon = springBeanProvider.getBean(item.getItemRenderer(), true).getImage(item);
    }
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
    if (!dragActive && icon != null) {
      TextureImpl.unbind();
      icon.draw(getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
    }
  }

  @Override
  protected void paintDragOverlay(GUI gui, int mouseX, int mouseY, int modifier) {
    if (icon != null) {
      final int innerWidth = getInnerWidth();
      final int innerHeight = getInnerHeight();
      TextureImpl.unbind();
      icon.draw(mouseX - innerWidth / 2, mouseY - innerHeight / 2, innerWidth, innerHeight);
    }
  }
}