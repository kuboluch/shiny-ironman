package kniemkiewicz.jqblocks.ingame.ui.workplace;

import kniemkiewicz.jqblocks.ingame.ui.widget.SelectItemPanel;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WorkplaceSelectPanel extends SelectItemPanel<WorkplaceDefinition> {

  public WorkplaceSelectPanel(List<WorkplaceDefinition> workplaceDefinitions) {
    super(workplaceDefinitions);
    this.setTitle("WorkplaceDefinition");
    this.setResizableAxis(ResizableAxis.BOTH);
  }
}
