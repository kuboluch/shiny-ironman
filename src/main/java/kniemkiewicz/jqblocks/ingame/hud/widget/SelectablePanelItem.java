package kniemkiewicz.jqblocks.ingame.hud.widget;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.hud.widget.model.PanelItemModel;

/**
 * User: qba
 * Date: 20.08.12
 */
public class SelectablePanelItem<T extends PanelItemModel> extends ResizableFrame {

  public static final AnimationState.StateKey STATE_HOVER = AnimationState.StateKey.get("hover");
  public static final AnimationState.StateKey STATE_SELECTED = AnimationState.StateKey.get("selected");

  T object;
  Icon icon;

  public SelectablePanelItem(T object, int width, int height) {
    assert object != null;
    assert width > 0;
    assert height > 0;
    this.object = object;
    this.setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    this.icon = new Icon(object.getImage(), width, height);
    add(this.icon);
    setTooltipContent(object.getName() + ": " + object.getDescription());
  }

  public T getObject() {
    return object;
  }

  @Override
  public int getPreferredInnerWidth() {
    return icon.getPreferredInnerWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    return icon.getPreferredInnerHeight();
  }

  @Override
  protected void layout() {
    icon.adjustSize();
  }

  public void deselect() {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_SELECTED, false);
  }

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEvent()) {
      final Event.Type eventType = evt.getType();

      if (eventType == Event.Type.MOUSE_WHEEL) {
        return false;
      }

      if (eventType == Event.Type.MOUSE_CLICKED) {
        if (getParent() instanceof SelectItemPanel) {
          SelectItemPanel panel = (SelectItemPanel) getParent();
          panel.select(this);
        }
      }

      if (eventType == Event.Type.MOUSE_ENTERED) {
        de.matthiasmann.twl.AnimationState as = getAnimationState();
        as.setAnimationState(STATE_HOVER, true);
      }

      if (eventType == Event.Type.MOUSE_EXITED) {
        de.matthiasmann.twl.AnimationState as = getAnimationState();
        as.setAnimationState(STATE_HOVER, false);
      }
    }

    if (super.handleEvent(evt)) {
      return true;
    }

    return false;
  }

  public void select() {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    as.setAnimationState(STATE_SELECTED, true);
  }
}
