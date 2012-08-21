package kniemkiewicz.jqblocks.ingame.ui.produce;

import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.item.ItemDefinition;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectObjectPanel;
import kniemkiewicz.jqblocks.ingame.ui.widget.SelectableObject;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionSelectPanel extends SelectObjectPanel<ItemDefinition> {
  private static final int margin = 5;

  ProductionController productionController;

  public ProductionSelectPanel(ProductionController productionController) {
    super(productionController.getItems());
    this.productionController = productionController;
    this.setTitle("Produce");
    this.setResizableAxis(ResizableAxis.BOTH);
    for (SelectableObject<ItemDefinition> selectableObject : selectableObjects) {
      selectableObject.setTheme("selectableproduceobject");
    }
  }

  @Override
  public void select(SelectableObject object) {
    super.select(object);
    productionController.changeSelected((ItemDefinition) object.getObject());
  }
}
