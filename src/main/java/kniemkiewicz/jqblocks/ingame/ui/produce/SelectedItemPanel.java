package kniemkiewicz.jqblocks.ingame.ui.produce;

import de.matthiasmann.twl.ResizableFrame;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.ui.widget.Icon;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;

/**
 * User: qba
 * Date: 22.08.12
 */
public class SelectedItemPanel extends ResizableFrame implements SelectListener<ItemDefinition> {

  final static int MIN_WIDTH = 60;
  final static int MIN_HEIGHT = 40;

  Icon icon;

  public SelectedItemPanel() {
    this.setResizableAxis(ResizableFrame.ResizableAxis.NONE);
  }

  @Override
  public int getPreferredInnerWidth() {
    int innerWidth = super.getPreferredInnerWidth();
    if (innerWidth < MIN_WIDTH) {
      return MIN_WIDTH;
    }
    return innerWidth;
  }

  @Override
  public int getPreferredInnerHeight() {
    int innerHeight = super.getPreferredInnerHeight();
    if (innerHeight < MIN_HEIGHT) {
      return MIN_HEIGHT;
    }
    return innerHeight;
  }

  @Override
  public void onSelect(ItemDefinition object) {
    removeChild(icon);
    icon = new Icon(object.getUIImage());
    add(icon);
  }
}
