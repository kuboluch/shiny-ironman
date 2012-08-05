package kniemkiewicz.jqblocks.ingame.ui.structure;

import de.matthiasmann.twl.ResizableFrame;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;

import java.util.ArrayList;
import java.util.List;

public class WorkplacePanel extends ResizableFrame {
  private static final int numItemsY = 5;
  private static final int margin = 1;

  WorkplaceController workplaceController;
  List<WorkplaceItem> workplaceItems = new ArrayList<WorkplaceItem>();

  public WorkplacePanel(WorkplaceController workplaceController) {
    this.workplaceController = workplaceController;
    this.setTitle("Workplace");
    this.setResizableAxis(ResizableFrame.ResizableAxis.NONE);

    for (Workplace workplace : workplaceController.getWorkplaces()) {
      WorkplaceItem item = new WorkplaceItem(workplace);
      workplaceItems.add(item);
      add(item);
    }
  }

  /*@Override
  public int getPreferredInnerWidth() {
    return (workplaceItems.get(0).getPreferredWidth() + margin) - margin;
  }

  @Override
  public int getPreferredInnerHeight() {
    return (workplaceItems.get(0).getPreferredHeight() + margin) * numItemsY - margin;
  }*/

  @Override
  protected void layout() {
    int itemHeight = workplaceItems.get(0).getPreferredHeight();

    for (int row = 0, y = getInnerY(), i = 0; row < numItemsY; row++) {
      //workplaceItems.get(i).adjustSize();
      workplaceItems.get(i).setPosition(getInnerX(), y);
      y += itemHeight + margin;
    }
  }
}
