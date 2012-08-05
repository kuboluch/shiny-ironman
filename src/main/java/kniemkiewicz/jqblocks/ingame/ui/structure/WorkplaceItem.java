package kniemkiewicz.jqblocks.ingame.ui.structure;

import de.matthiasmann.twl.Widget;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceItem extends Widget {

  Workplace workplace;

  public WorkplaceItem(Workplace workplace) {
    this.workplace = workplace;
    setSize(20, 20);
  }

  @Override
  public int getPreferredWidth() {
    return 20;
  }

  @Override
  public int getPreferredHeight() {
    return 20;
  }

  /*@Override
  protected void paintWidget(GUI gui) {
    if (workplace != null) {
      workplace.getIcon().draw(getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
    }
  }*/
}
