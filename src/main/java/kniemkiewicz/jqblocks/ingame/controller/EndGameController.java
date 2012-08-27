package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class EndGameController implements InputListener {

  Log logger = LogFactory.getLog(EndGameController.class);

  boolean gameShouldRestart;
  boolean gameShouldBeLoaded;
  boolean gameShouldEnd;

  @Autowired
  SaveGameListener saveGameListener;

  @Autowired
  World world;

  public void listen(Input input, int delta) {
    // Do not try any sudden moves just after start/restart.
    if (world.getTimestamp() < 1000) return;
    if (KeyboardUtils.isExitKeyPressed(input)) {
      gameShouldEnd = true;
      logger.info("Exit key pressed");
    }
    if (KeyboardUtils.isRestartKeyPressed(input)) {
      gameShouldRestart = true;
      logger.info("Restart key pressed");
    }
    if (KeyboardUtils.isLoadKeyPressed(input)) {
      if (saveGameListener.savegameExists()) {
        gameShouldRestart = true;
        gameShouldBeLoaded = true;
      } else {
        // TODO: This should be user visible.
        logger.error("Savegame does not exist.");
      }
    }
  }

  public boolean isGameShouldRestart() {
    return gameShouldRestart;
  }

  public boolean isGameShouldEnd() {
    return gameShouldEnd;
  }

  public boolean isGameShouldBeLoaded() {
    return gameShouldBeLoaded;
  }
}
