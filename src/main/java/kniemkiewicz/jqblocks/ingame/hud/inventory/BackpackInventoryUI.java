package kniemkiewicz.jqblocks.ingame.hud.inventory;

import de.matthiasmann.twl.Event;
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

  @Override
  // UI cannot consume MouseWheelEvent, it is has to be handled by EventBus
  protected boolean handleEvent(Event evt) {
    boolean result = super.handleEvent(evt);
    return result && evt.isMouseEventNoWheel();
  }
}
