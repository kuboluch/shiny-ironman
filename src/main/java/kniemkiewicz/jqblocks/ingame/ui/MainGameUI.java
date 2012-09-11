package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.ui.inventory.InventoryUI;
import kniemkiewicz.jqblocks.ingame.ui.inventory.QuickItemInventoryUI;
import kniemkiewicz.jqblocks.ingame.ui.produce.ProductionUI;
import kniemkiewicz.jqblocks.ingame.ui.workplace.WorkplaceUI;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceController;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MainGameUI {

  RootPane rootPane;

  @Autowired
  WorkplaceController workplaceController;

  @Autowired
  ProductionController productionController;

  @Autowired
  EventBus eventBus;

  @Autowired
  private WorkplaceUI workplaceUI;

  @Autowired
  private ProductionUI productionUI;

  @Autowired
  private QuickItemInventoryUI quickItemInventoryUI;

  @Autowired
  private InventoryUI inventoryUI;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;
    init();
  }

  public void init() {
    rootPane.add(workplaceUI);
    rootPane.add(productionUI);
    rootPane.add(quickItemInventoryUI);
    rootPane.add(inventoryUI);
  }

  public void layoutUI() {
    final int HEIGHT = 200;
    workplaceUI.adjustSize();
    workplaceUI.setPosition(5, rootPane.getHeight() - HEIGHT - 40);
    productionUI.adjustSize();
    //productionUI.setPosition((rootPane.getWidth() - productionUI.getWidth()) / 2, (rootPane.getHeight() - productionUI.getHeight()) / 2);
    productionUI.setPosition(rootPane.getWidth() - productionUI.getWidth() - 5, rootPane.getHeight() - HEIGHT - 40);
    quickItemInventoryUI.adjustSize();
    quickItemInventoryUI.setPosition(rootPane.getWidth() - quickItemInventoryUI.getWidth() - 5, 5);
    inventoryUI.adjustSize();
    inventoryUI.setPosition((rootPane.getWidth() - inventoryUI.getWidth()) / 2, (rootPane.getHeight() - inventoryUI.getHeight()) / 2);
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

  public boolean isInventoryWidgetVisible() {
    return inventoryUI.isVisible();
  }

  public void showInventoryWidget() {
    inventoryUI.setVisible(true);
  }

  public void hideInventoryWidget() {
    inventoryUI.setVisible(false);
  }
}
