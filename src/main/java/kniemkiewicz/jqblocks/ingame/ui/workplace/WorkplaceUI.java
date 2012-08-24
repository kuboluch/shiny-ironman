package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;

/**
 * User: qba
 * Date: 20.08.12
 */
public class WorkplaceUI extends ResizableFrame {

  private final static int HEIGHT = 200;

  private ScrollPane scrollPane;
  private WorkplaceSelectPanel workplaceSelectPanel;

  public WorkplaceUI(WorkplaceController workplaceController) {
    workplaceSelectPanel = new WorkplaceSelectPanel(workplaceController);
    scrollPane = new ScrollPane(workplaceSelectPanel);
    scrollPane.setVisible(true);
    scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
    scrollPane.setExpandContentSize(true);
    scrollPane.setTheme("scrollPane-noscrollbar");
    setVisible(false);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    //setTheme("noframe");
    setTheme("resizableframe");
    add(scrollPane);
  }

  @Override
  protected void layout() {
    super.layout();
    workplaceSelectPanel.adjustSize();
    scrollPane.setSize(workplaceSelectPanel.getPreferredWidth(), HEIGHT);
  }

  @Override
  public int getPreferredInnerHeight() {
    return HEIGHT;
  }

  public void deselectAll() {
    workplaceSelectPanel.deselectAll();
  }
}
