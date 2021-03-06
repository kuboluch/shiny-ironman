package kniemkiewicz.jqblocks.ingame.hud;

import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;
import kniemkiewicz.jqblocks.ingame.hud.inventory.BackpackInventoryUI;
import kniemkiewicz.jqblocks.ingame.hud.inventory.QuickItemInventoryUI;
import kniemkiewicz.jqblocks.ingame.hud.inventory.ResourceInventoryUI;
import kniemkiewicz.jqblocks.ingame.hud.produce.ProductionUI;
import kniemkiewicz.jqblocks.ingame.hud.workplace.WorkplaceUI;
import kniemkiewicz.jqblocks.ingame.object.workplace.WorkplaceController;
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

  @Autowired
  WorkplaceUI workplaceUI;

  @Autowired
  ProductionUI productionUI;

  @Autowired
  QuickItemInventoryUI quickItemInventoryUI;

  @Autowired
  BackpackInventoryUI backpackInventoryUI;

  @Autowired
  ResourceInventoryUI resourceInventoryUI;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;
    init();
  }

  public void init() {
    rootPane.add(workplaceUI);
    rootPane.add(productionUI);
    rootPane.add(quickItemInventoryUI);
    rootPane.add(backpackInventoryUI);
    rootPane.add(resourceInventoryUI);
  }

  public RootPane getRootPane() {
    return rootPane;
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
    backpackInventoryUI.adjustSize();
    backpackInventoryUI.setPosition((rootPane.getWidth() - backpackInventoryUI.getWidth()) / 2, (rootPane.getHeight() - backpackInventoryUI.getHeight()) / 2);
    resourceInventoryUI.adjustSize();
    resourceInventoryUI.setPosition(rootPane.getWidth() - resourceInventoryUI.getWidth() - 5, 5 + quickItemInventoryUI.getHeight() + 5);
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
    return backpackInventoryUI.isVisible();
  }

  public void showInventoryWidget() {
    backpackInventoryUI.setVisible(true);
  }

  public void hideInventoryWidget() {
    backpackInventoryUI.setVisible(false);
  }
}
