package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
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

  Label[] label;

  public QuickItemInventoryUI() {
  }

  @PostConstruct
  public void init() {
    setVisible(true);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(quickItemInventoryPanel);
    setTheme("panel");

    label = new Label[10];
    for (int i = 0, x = getInnerX(); i < 10; i++) {
      label[i] = new Label(String.valueOf((i+1)%10));
      label[i].setTheme("quickitemlabel");
      add(label[i]);
    }
  }

  @Override
  protected void layout() {
    super.layout();
    quickItemInventoryPanel.adjustSize();

    int xSlotsNumber = quickItemInventoryPanel.getXSlotsNumber();
    int slotSpacing = quickItemInventoryPanel.getSlotSpacing();
    int slotWidth = (quickItemInventoryPanel.getPreferredWidth() - (xSlotsNumber-1) * slotSpacing) / xSlotsNumber;
    int slotHeight = quickItemInventoryPanel.getPreferredHeight();
    for (int i = 0, x = getInnerX(); i < xSlotsNumber; i++) {
      label[i].adjustSize();
      label[i].setSize(slotWidth, label[i].getHeight());
      label[i].setPosition(x, getInnerY() + slotHeight);
      x += slotWidth + slotSpacing;
    }
  }

  @Override
  public int getPreferredInnerHeight() {
    return quickItemInventoryPanel.getPreferredHeight() + label[0].getPreferredHeight();
  }

  @Override
  // UI cannot consume MouseWheelEvent, it is has to be handled by EventBus
  protected boolean handleEvent(Event evt) {
    boolean result = super.handleEvent(evt);
    return result && evt.isMouseEventNoWheel();
  }
}
