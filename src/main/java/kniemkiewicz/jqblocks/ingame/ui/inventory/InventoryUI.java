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

  private final static int HEIGHT = 200;

  @Autowired
  InventoryPanel inventoryPanel;

  public InventoryUI() {
  }

  @PostConstruct
  public void init() {
    setVisible(false);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(inventoryPanel);
    setTheme("panel");
  }

  @Override
  protected void layout() {
    super.layout();
    inventoryPanel.adjustSize();
  }
}
