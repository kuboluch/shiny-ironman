package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.ResizableFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 05.09.12
 */
@Component
public class InventoryUI extends ResizableFrame {

  @Autowired
  BackpackInventoryPanel backpackInventoryPanel;

  public InventoryUI() {
  }

  @PostConstruct
  public void init() {
    setVisible(false);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(backpackInventoryPanel);
    setTheme("panel");
  }

  @Override
  protected void layout() {
    super.layout();
    backpackInventoryPanel.adjustSize();
  }
}
