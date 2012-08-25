package kniemkiewicz.jqblocks.ingame.ui.produce;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionUI extends ResizableFrame {

  private final static int HEIGHT = 200;
  private final static int GAP_WIDTH = 20;
  private final static int GAP_HEIGHT = 10;
  private final static int ICON_SIZE = 40;

  private ScrollPane scrollPane;
  private ProductionSelectPanel productionSelectPanel;
  private SelectedProductionPanel selectedProductionPanel;
  private Button productionButton;
  private ProductionRequirementsPanel productionRequirementsPanel;

  public ProductionUI(final ProductionController productionController, EventBus eventBus) {
    // Select item for production
    productionSelectPanel = new ProductionSelectPanel(productionController.getItemDefinitions());
    scrollPane = new ScrollPane(productionSelectPanel);
    scrollPane.setVisible(true);
    scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
    scrollPane.setExpandContentSize(true);
    scrollPane.setTheme("scrollPane-noscrollbar");
    setVisible(false);
    setResizableAxis(ResizableFrame.ResizableAxis.NONE);
    //setTheme("noframe");
    setTheme("resizableframe");
    add(scrollPane);

    // Selected item view
    selectedProductionPanel = new SelectedProductionPanel(ICON_SIZE, ICON_SIZE);
    add(selectedProductionPanel);

    // Produce button
    productionButton = new Button();
    productionButton.setText("Produce");
    productionButton.addCallback(new Runnable() {
      @Override
      public void run() {
        if (productionController.getSelectedItem() != null) {
          productionController.produce();
        }
      }
    });
    add(productionButton);

    // Production requirements
    productionRequirementsPanel = new ProductionRequirementsPanel();
    add(productionRequirementsPanel);

    productionSelectPanel.addSelectListener(selectedProductionPanel);
    productionSelectPanel.addSelectListener(productionController);
    productionSelectPanel.addSelectListener(productionRequirementsPanel);
    eventBus.addListener(productionRequirementsPanel);
  }

  @Override
  protected void layout() {
    super.layout();
    productionSelectPanel.adjustSize();
    scrollPane.setSize(productionSelectPanel.getPreferredWidth(), HEIGHT);
    selectedProductionPanel.adjustSize();
    selectedProductionPanel.setPosition(getInnerX() + productionSelectPanel.getWidth() + GAP_WIDTH, getInnerY() + (HEIGHT - selectedProductionPanel.getHeight()) / 2);
    productionButton.adjustSize();
    productionButton.setPosition(selectedProductionPanel.getX(), selectedProductionPanel.getY() + selectedProductionPanel.getHeight() + GAP_HEIGHT);
    productionRequirementsPanel.adjustSize();
    productionRequirementsPanel.setPosition(getInnerX() + productionSelectPanel.getWidth() + GAP_WIDTH + selectedProductionPanel.getWidth() + GAP_WIDTH, getInnerY());
  }

  @Override
  public int getPreferredInnerWidth() {
    int preferredWidth = productionSelectPanel.getPreferredWidth();
    preferredWidth += GAP_WIDTH + selectedProductionPanel.getPreferredWidth();
    preferredWidth += GAP_WIDTH + productionRequirementsPanel.getPreferredWidth();
    return preferredWidth;
  }

  @Override
  public int getPreferredInnerHeight() {
    return HEIGHT;
  }

  public void deselectAll() {
    productionSelectPanel.deselectAll();
  }

}
