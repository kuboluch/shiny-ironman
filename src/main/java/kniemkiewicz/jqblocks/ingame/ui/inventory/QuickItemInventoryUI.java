package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.ResizableFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class QuickItemInventoryUI extends ResizableFrame {

  @Autowired
  QuickItemInventoryPanel quickItemInventoryPanel;

  public QuickItemInventoryUI() {
  }

  @PostConstruct
  public void init() {
    setVisible(true);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(quickItemInventoryPanel);
    setTheme("panel");
  }

  @Override
  protected void layout() {
    super.layout();
    quickItemInventoryPanel.adjustSize();
  }
}
