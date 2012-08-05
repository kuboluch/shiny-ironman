package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.ui.structure.WorkplacePanel;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainGameUI {

  RootPane rootPane;

  @Autowired
  WorkplaceController workplaceController;

  private WorkplacePanel workplacePanel;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;

    workplacePanel = new WorkplacePanel(workplaceController);
    rootPane.add(workplacePanel);
  }

  public void layoutUI() {
    workplacePanel.adjustSize();
    workplacePanel.setPosition(5, rootPane.getHeight() - 200 - 5);
    workplacePanel.setSize(150, 200);
    workplacePanel.setInnerSize(150, 200);
    workplacePanel.setVisible(false);
  }

  public boolean isStructureWidgetVisible() {
    return workplacePanel.isVisible();
  }

  public void showStructureWidget() {
    workplacePanel.setVisible(true);
  }

  public void hideStructureWidget() {
    workplacePanel.setVisible(false);
  }
}
