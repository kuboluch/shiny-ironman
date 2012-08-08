package kniemkiewicz.jqblocks.ingame.ui;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.textarea.TextAreaModel;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.ingame.ui.workplace.WorkplacePanel;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainGameUI {

  RootPane rootPane;

  @Autowired
  WorkplaceController workplaceController;

  private ScrollPane scrollPane;
  private WorkplacePanel workplacePanel;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;

    workplacePanel = new WorkplacePanel(workplaceController);
    scrollPane = new ScrollPane(workplacePanel);
    scrollPane.setVisible(true);
    scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
    scrollPane.setExpandContentSize(true);
    scrollPane.setTheme("scrollPane-noscrollbar");
    rootPane.add(scrollPane);
  }

  public void layoutUI() {
    workplacePanel.adjustSize();
    scrollPane.setPosition(5, rootPane.getHeight() - 200 - 5);
    scrollPane.setSize(workplacePanel.getPreferredWidth(), 200);
    scrollPane.setVisible(true);
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
