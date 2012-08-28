package kniemkiewicz.jqblocks.ingame.ui.produce;

import de.matthiasmann.twl.ResizableFrame;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.production.AvailableItemsChangeEvent;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.ui.widget.Icon;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectListener;
import kniemkiewicz.jqblocks.util.Collections3;

import java.util.Arrays;
import java.util.List;

/**
 * User: qba
 * Date: 22.08.12
 */
public class SelectedProductionPanel extends ResizableFrame implements SelectListener<ItemDefinition> {

  final static int MIN_WIDTH = 60;
  final static int MIN_HEIGHT = 40;

  final static int DEFAULT_ICON_SIZE = 40;

  Icon icon;
  int width;
  int height;

  public SelectedProductionPanel(int width, int height) {
    this.setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    this.width = width;
    this.height = height;
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
    clear();
    icon = new Icon(object.getImage(), width, height);
    add(icon);
  }

  public void clear() {
    removeChild(icon);
  }
}
