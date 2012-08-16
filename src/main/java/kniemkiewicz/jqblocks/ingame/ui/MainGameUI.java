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

  private ResizableFrame workplaceFrame;
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
    workplaceFrame = new ResizableFrame();
    workplaceFrame.setVisible(false);
    workplaceFrame.setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    workplaceFrame.setTheme("noframe");
    workplaceFrame.add(scrollPane);
    rootPane.add(workplaceFrame);
  }

  public void layoutUI() {
    workplacePanel.adjustSize();
    final int HEIGHT = 200;
    scrollPane.setSize(workplacePanel.getPreferredWidth(), HEIGHT);
    workplaceFrame.adjustSize();
    workplaceFrame.setSize(workplaceFrame.getWidth(), HEIGHT);
    workplaceFrame.setPosition(5, rootPane.getHeight() - HEIGHT - 40);
  }

  public boolean isWorkplaceWidgetVisible() {
    return workplaceFrame.isVisible();
  }

  public void showWorkplaceWidget() {
    workplaceFrame.setVisible(true);
  }

  public void hideWorkplaceWidget() {
    workplaceFrame.setVisible(false);
  }

  public void resetWorkplaceWidget() {
    workplacePanel.deselectAll();
  }
}
