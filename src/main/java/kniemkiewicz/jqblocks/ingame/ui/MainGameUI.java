package kniemkiewicz.jqblocks.ingame.ui;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import kniemkiewicz.jqblocks.twl.RootPane;
import org.springframework.stereotype.Component;

@Component
public class MainGameUI {

  RootPane rootPane;

  private ResizableFrame constructFrame;
  private Button button;

  public void createUI(RootPane rootPane) {
    this.rootPane = rootPane;

    constructFrame = new ResizableFrame();
    constructFrame.setTitle("Construct");
    constructFrame.setResizableAxis(ResizableFrame.ResizableAxis.NONE);

    rootPane.add(constructFrame);
  }

  public void layoutUI() {
    constructFrame.adjustSize();
    constructFrame.setPosition(5, rootPane.getHeight() - 200 - 5);
    constructFrame.setSize(150, 200);
    constructFrame.setVisible(false);
  }

  public boolean isConstructWidgetVisible() {
    return constructFrame.isVisible();
  }

  public void showConstructWidget() {
    constructFrame.setVisible(true);
  }

  public void hideConstructWidget() {
    constructFrame.setVisible(false);
  }
}
