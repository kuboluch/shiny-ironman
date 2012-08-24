package kniemkiewicz.jqblocks.ingame.ui.workplace;

import kniemkiewicz.jqblocks.ingame.ui.widget.SelectObjectPanel;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectableObject;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;

public class WorkplaceSelectPanel extends SelectObjectPanel<WorkplaceDefinition> {

  WorkplaceController workplaceController;

  public WorkplaceSelectPanel(WorkplaceController workplaceController) {
    super(workplaceController.getWorkplaceDefinitions());
    this.workplaceController = workplaceController;
    this.setTitle("WorkplaceDefinition");
    this.setResizableAxis(ResizableAxis.BOTH);
  }

  @Override
  public void select(SelectableObject object) {
    super.select(object);
    workplaceController.changeSelected((WorkplaceDefinition) object.getObject());
  }
}
