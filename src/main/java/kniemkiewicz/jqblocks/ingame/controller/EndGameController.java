package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import org.newdawn.slick.Input;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class EndGameController implements InputListener {
  boolean gameShouldRestart;
  boolean gameShouldEnd;
  public void listen(Input input, int delta) {
    if (KeyboardUtils.isExitKeyPressed(input)) {
      gameShouldEnd = true;
    }
    if (KeyboardUtils.isRestartKeyPressed(input)) {
      gameShouldRestart = true;
    }
  }

  public boolean isGameShouldRestart() {
    return gameShouldRestart;
  }

  public boolean isGameShouldEnd() {
    return gameShouldEnd;
  }
}
