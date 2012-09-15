package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 05.09.12
 */
@Component
public class BackpackInventoryUI extends ResizableFrame {

  @Autowired
  BackpackInventoryPanel backpackInventoryPanel;

  public BackpackInventoryUI() {
  }

  @PostConstruct
  public void init() {
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(backpackInventoryPanel);
    setTheme("panel");
    setVisible(false);
  }

  @Override
  protected void layout() {
    super.layout();
    backpackInventoryPanel.adjustSize();
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    for (int i = 0, n = getNumChildren(); i < n; i++) {
      Widget child = getChild(i);
      child.setVisible(visible);
    }
  }
}
