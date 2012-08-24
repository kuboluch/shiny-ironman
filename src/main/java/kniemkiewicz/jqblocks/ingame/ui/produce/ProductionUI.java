package kniemkiewicz.jqblocks.ingame.ui.produce;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import kniemkiewicz.jqblocks.ingame.production.ProductionController;

/**
 * User: qba
 * Date: 20.08.12
 */
public class ProductionUI extends ResizableFrame {

  private final static int HEIGHT = 200;
  private final static int GAP_WIDTH = 20;

  private ScrollPane scrollPane;
  private ProductionSelectPanel productionSelectPanel;
  private SelectedItemPanel selectedItemPanel;

  public ProductionUI(ProductionController productionController) {
    productionSelectPanel = new ProductionSelectPanel(productionController);
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
    selectedItemPanel = new SelectedItemPanel();
    productionSelectPanel.addSelectListener(selectedItemPanel);
    add(selectedItemPanel);
  }

  @Override
  protected void layout() {
    super.layout();
    productionSelectPanel.adjustSize();
    scrollPane.setSize(productionSelectPanel.getPreferredWidth(), HEIGHT);
    selectedItemPanel.adjustSize();
    selectedItemPanel.setPosition(getInnerX() + productionSelectPanel.getWidth() + GAP_WIDTH, getInnerY() + (HEIGHT - selectedItemPanel.getHeight()) / 2);
  }

  @Override
  public int getPreferredInnerWidth() {
    return productionSelectPanel.getPreferredWidth() + GAP_WIDTH + selectedItemPanel.getPreferredWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    return HEIGHT;
  }

  public void deselectAll() {
    productionSelectPanel.deselectAll();
  }

}
