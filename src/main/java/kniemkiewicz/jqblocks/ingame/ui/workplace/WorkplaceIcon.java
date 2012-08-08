package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.ui.renderer.Image;

/**
 * User: qba
 * Date: 07.08.12
 */
public class WorkplaceIcon extends Widget {

  Image image;

  public WorkplaceIcon(Image image) {
    assert image != null;
    this.image = image;
  }

  @Override
  public int getPreferredInnerWidth() {
    return image.getWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    return image.getHeight();
  }

  @Override
  protected void paintWidget(GUI gui) {
    image.draw(getAnimationState(), getInnerX(), getInnerY());
  }
}
