package kniemkiewicz.jqblocks.ingame.ui.inventory;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 12.09.12
 */
@Component
public class ResourceInventoryUI extends ResizableFrame {

  @Autowired
  ResourceInventoryPanel resourceInventoryPanel;

  Label[] label;

  public ResourceInventoryUI() {
  }

  @PostConstruct
  public void init() {
    setVisible(true);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    add(resourceInventoryPanel);
    setTheme("panel");

    label = new Label[2];
    for (int i = 0, x = getInnerX(); i < 2; i++) {
      label[i] = new Label("F"+String.valueOf(i+1));
      label[i].setTheme("quickitemlabel");
      add(label[i]);
    }
  }

  @Override
  protected void layout() {
    super.layout();
    resourceInventoryPanel.adjustSize();

    int xSlotsNumber = resourceInventoryPanel.getXSlotsNumber();
    int slotSpacing = resourceInventoryPanel.getSlotSpacing();
    int slotWidth = (resourceInventoryPanel.getPreferredWidth() - (xSlotsNumber-1) * slotSpacing) / xSlotsNumber;
    int slotHeight = resourceInventoryPanel.getPreferredHeight();
    for (int i = 0, x = getInnerX(); i < xSlotsNumber; i++) {
      label[i].adjustSize();
      label[i].setSize(slotWidth, label[i].getHeight());
      label[i].setPosition(x, getInnerY() + slotHeight);
      x += slotWidth + slotSpacing;
    }
  }

  @Override
  public int getPreferredInnerHeight() {
    return resourceInventoryPanel.getPreferredHeight() + label[0].getPreferredHeight();
  }
}