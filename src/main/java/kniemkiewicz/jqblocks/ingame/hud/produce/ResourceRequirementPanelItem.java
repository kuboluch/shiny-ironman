package kniemkiewicz.jqblocks.ingame.hud.produce;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.production.ResourceRequirement;
import kniemkiewicz.jqblocks.ingame.hud.widget.Icon;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * User: qba
 * Date: 26.08.12
 */
@Configurable
public class ResourceRequirementPanelItem extends ResizableFrame {

  static final int MARGIN = 5;

  public static final AnimationState.StateKey STATE_FULFILLED = AnimationState.StateKey.get("fulfilled");
  public static final AnimationState.StateKey STATE_NOTFULFILLED = AnimationState.StateKey.get("notfulfilled");

  ResourceRequirement resourceRequirement;
  Icon icon;
  Label label;

  public ResourceRequirementPanelItem(ResourceRequirement resourceRequirement, int itemWidth, int itemHeight) {
    this.resourceRequirement = resourceRequirement;
    icon = new Icon(resourceRequirement.getImage(), itemWidth, itemHeight);
    add(this.icon);
    label = new Label();
    label.setLabelFor(this);
    label.setAlignment(Alignment.CENTER);
    label.setText(String.valueOf(resourceRequirement.getResource().getAmount()) + "x");
    add(label);
    setTooltipContent(resourceRequirement.getName() + ": " + resourceRequirement.getDescription());
    updateState();
  }

  @Override
  public int getPreferredInnerHeight() {
    int preferredHeight = icon.getPreferredHeight();
    preferredHeight += MARGIN + label.getPreferredHeight();
    return preferredHeight;
  }

  @Override
  protected void layout() {
    icon.adjustSize();
    label.adjustSize();
    label.setPosition(getInnerX() + icon.getWidth() / 2 - label.getWidth() / 2, getInnerY() + icon.getHeight() + MARGIN);
  }

  public void updateState() {
    de.matthiasmann.twl.AnimationState as = getAnimationState();
    if (resourceRequirement.isFulfilled()) {
      as.setAnimationState(STATE_FULFILLED, true);
      as.setAnimationState(STATE_NOTFULFILLED, false);
    } else {
      as.setAnimationState(STATE_NOTFULFILLED, true);
      as.setAnimationState(STATE_FULFILLED, false);
    }
  }
}
