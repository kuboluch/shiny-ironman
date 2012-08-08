package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.ui.MainGameUI;
import org.newdawn.slick.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UIController implements InputListener {

  @Autowired
  MainGameUI mainGameUI;

  boolean contructKeyProcessed = false;

  @Override
  public void listen(Input input, int delta) {
    if (KeyboardUtils.isConstructMenuKeyReleased(input)) {
      contructKeyProcessed = false;
    }

    if (KeyboardUtils.isConstructMenuKeyPressed(input) && !contructKeyProcessed) {
      if (mainGameUI.isWorkplaceWidgetVisible()) {
        mainGameUI.hideWorkplaceWidget();
      } else {
        mainGameUI.showWorkplaceWidget();
      }
      contructKeyProcessed = true;
    }
  }
}
