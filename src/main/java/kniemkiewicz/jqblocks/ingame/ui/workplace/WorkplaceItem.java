package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceItem extends ResizableFrame {
  public static final AnimationState.StateKey STATE_HOVER = AnimationState.StateKey.get("hover");
  public static final AnimationState.StateKey STATE_SELECTED = AnimationState.StateKey.get("selected");

  Workplace workplace;

  WorkplaceIcon icon;

  public WorkplaceItem(Workplace workplace) {
    assert workplace != null;
    this.workplace = workplace;
    this.setResizableAxis(ResizableAxis.NONE);
    this.icon = new WorkplaceIcon(workplace.getUIImage());
    add(icon);
    setTooltipContent(workplace.getName() + ": " + workplace.getDescription());
  }

  public Workplace getWorkplace() {
    return workplace;
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
        if (getParent() instanceof WorkplacePanel) {
          WorkplacePanel panel = ((WorkplacePanel) getParent());
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
