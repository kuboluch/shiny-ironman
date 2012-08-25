package kniemkiewicz.jqblocks.ingame.ui.produce;

import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectItemPanel;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectablePanelItem;

import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionSelectPanel extends SelectItemPanel<ItemDefinition> {
  static final int ITEM_WIDTH = 40;
  static final int ITEM_HEIGHT = 40;

  public ProductionSelectPanel(List<ItemDefinition> items) {
    super(items, ITEM_WIDTH, ITEM_HEIGHT);
    this.setTitle("Produce");
    this.setResizableAxis(ResizableAxis.BOTH);
    for (SelectablePanelItem<ItemDefinition> panelItem : panelItems) {
      panelItem.setTheme("selectablepanelitem-production");
    }
  }
}
