package kniemkiewicz.jqblocks.ingame.ui.workplace;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 20.08.12
 */
@Component
public class WorkplaceUI extends ResizableFrame {

  private final static int HEIGHT = 200;

  @Autowired
  WorkplaceController workplaceController;

  private ScrollPane scrollPane;
  private WorkplaceSelectPanel workplaceSelectPanel;

  public WorkplaceUI() {
  }

  @PostConstruct
  public void init() {
    workplaceSelectPanel = new WorkplaceSelectPanel(workplaceController.getWorkplaceDefinitions());
    scrollPane = new ScrollPane(workplaceSelectPanel);
    scrollPane.setVisible(true);
    scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
    scrollPane.setExpandContentSize(true);
    scrollPane.setTheme("scrollPane-noscrollbar");
    setVisible(false);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    setTheme("noframe");
    add(scrollPane);
    workplaceSelectPanel.addSelectListener(workplaceController);
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
