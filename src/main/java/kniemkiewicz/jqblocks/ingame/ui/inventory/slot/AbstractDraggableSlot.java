package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * User: qba
 * Date: 10.09.12
 */
public abstract class AbstractDraggableSlot<T> extends Widget implements DraggableSlot<T> {

  public static final AnimationState.StateKey STATE_DRAG_ACTIVE = AnimationState.StateKey.get("dragActive");
  public static final AnimationState.StateKey STATE_DROP_OK = AnimationState.StateKey.get("dropOk");
  public static final AnimationState.StateKey STATE_DROP_BLOCKED = AnimationState.StateKey.get("dropBlocked");

  protected DragListener listener;
  protected boolean dragActive;

  public abstract boolean canDrop(T item);

  public void setListener(DragListener listener) {
    this.listener = listener;
  }

  public boolean isDragActive() {
    return dragActive;
  }

  public void resetDropState() {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_DROP_OK, false);
    as.setAnimationState(STATE_DROP_BLOCKED, false);
    dragActive = false;
  }

  public boolean dropFrom(DraggableSlot<T> slot) {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    boolean dropValid = (this == slot || canDrop(slot.getModel()));
    as.setAnimationState(STATE_DROP_OK, dropValid);
    as.setAnimationState(STATE_DROP_BLOCKED, !dropValid);
    return dropValid;
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
  protected void paintDragOverlay(GUI gui, int mouseX, int mouseY, int modifier) {
    if (dragActive) {
      TextureImpl.unbind();
      renderModel(gui, mouseX - getInnerWidth() / 2, mouseY - getInnerHeight() / 2);
      Renderer.get().glEnable(SGL.GL_TEXTURE_2D);
    }
  }

  public abstract void renderModel(GUI gui, int x, int y);
}
