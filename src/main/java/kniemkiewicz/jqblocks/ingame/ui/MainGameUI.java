package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.ui.produce.ProductionUI;
import kniemkiewicz.jqblocks.ingame.ui.workplace.WorkplaceUI;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainGameUI {

  RootPane rootPane;

  @Autowired
  WorkplaceController workplaceController;

  @Autowired
  ProductionController productionController;

  @Autowired
  EventBus eventBus;

  private WorkplaceUI workplaceUI;
  private ProductionUI productionUI;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;

    workplaceUI = new WorkplaceUI(workplaceController);
    productionUI = new ProductionUI(productionController, eventBus);

    rootPane.add(workplaceUI);
    rootPane.add(productionUI);
  }

  public void layoutUI() {
    final int HEIGHT = 200;
    workplaceUI.adjustSize();
    workplaceUI.setPosition(5, rootPane.getHeight() - HEIGHT - 40);
    productionUI.adjustSize();
    //productionUI.setPosition((rootPane.getWidth() - productionUI.getWidth()) / 2, (rootPane.getHeight() - productionUI.getHeight()) / 2);
    productionUI.setPosition(rootPane.getWidth() - productionUI.getWidth() - 5, rootPane.getHeight() - HEIGHT - 40);
  }

  public boolean isWorkplaceWidgetVisible() {
    return workplaceUI.isVisible();
  }

  public void showWorkplaceWidget() {
    workplaceUI.setVisible(true);
  }

  public void hideWorkplaceWidget() {
    workplaceUI.setVisible(false);
  }

  public void resetWorkplaceWidget() {
    workplaceUI.deselectAll();
  }

  public boolean isConstructWidgetVisible() {
    return productionUI.isVisible();
  }

  public void showConstructWidget() {
    productionUI.setVisible(true);
  }

  public void hideConstructWidget() {
    productionUI.setVisible(false);
  }
}
