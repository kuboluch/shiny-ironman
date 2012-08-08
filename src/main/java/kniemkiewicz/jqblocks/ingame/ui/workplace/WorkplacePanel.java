package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;

import java.util.ArrayList;
import java.util.List;

public class WorkplacePanel extends ResizableFrame {
  private static final int margin = 10;

  WorkplaceController workplaceController;
  List<WorkplaceItem> workplaceItems = new ArrayList<WorkplaceItem>();

  public WorkplacePanel(WorkplaceController workplaceController) {
    this.workplaceController = workplaceController;
    this.setTitle("Workplace");
    this.setResizableAxis(ResizableAxis.BOTH);

    for (Workplace workplace : workplaceController.getWorkplaces()) {
      WorkplaceItem item = new WorkplaceItem(workplace);
      workplaceItems.add(item);
      add(item);
    }
  }

  @Override
  public int getPreferredInnerWidth() {
    return workplaceItems.get(0).getPreferredWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    int itemHeight = workplaceItems.get(0).getPreferredHeight();
    return itemHeight * workplaceItems.size() + margin * (workplaceItems.size() - 1);
  }

  @Override
  protected void layout() {
    int itemHeight = workplaceItems.get(0).getPreferredHeight();

    for (int row = 0, y = getInnerY(); row < workplaceItems.size(); row++) {
      workplaceItems.get(row).adjustSize();
      workplaceItems.get(row).setPosition(getInnerX(), y);
      y += itemHeight + margin;
    }
  }

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEvent()) {
      final Event.Type eventType = evt.getType();

      if (eventType == Event.Type.MOUSE_WHEEL) {
        return false;
      }
    }

    if (super.handleEvent(evt)) {
      return true;
    }

    return false;
  }
}
