package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Image;
import kniemkiewicz.jqblocks.ingame.ui.ImageUtils;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceItem extends Widget {
  Workplace workplace;

//  private final TextArea textArea;

  public WorkplaceItem(Workplace workplace) {
    this.workplace = workplace;
    /*this.textArea = new TextArea(new SimpleTextAreaModel(workplace.getName()));
    add(textArea);*/
  }

  @Override
  public int getPreferredInnerWidth() {
    assert workplace != null;
    return workplace.getUIImage().getWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    assert workplace != null;
    return workplace.getUIImage().getHeight();
  }

  @Override
  protected void paintWidget(GUI gui) {
    assert workplace != null;
    workplace.getUIImage().draw(getAnimationState(), getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
  }
}
