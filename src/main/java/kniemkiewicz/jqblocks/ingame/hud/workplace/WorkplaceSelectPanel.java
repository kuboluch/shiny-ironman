package kniemkiewicz.jqblocks.ingame.hud.workplace;

import kniemkiewicz.jqblocks.ingame.hud.widget.SelectItemPanel;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;

import java.util.List;

public class WorkplaceSelectPanel extends SelectItemPanel<WorkplaceDefinition> {

  public WorkplaceSelectPanel(List<WorkplaceDefinition> workplaceDefinitions) {
    super(workplaceDefinitions);
    this.setTitle("WorkplaceDefinition");
    this.setResizableAxis(ResizableAxis.BOTH);
  }
}
