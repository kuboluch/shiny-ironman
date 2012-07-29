package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.InputListener;
import kniemkiewicz.jqblocks.ingame.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * User: knie
 * Date: 7/28/12
 */
@Component
public class SaveGameListener implements InputListener {

  static Log logger = LogFactory.getLog(SaveGameListener.class);

  public static final String FILENAME = "savegame.dat";

  @Autowired
  World world;

  // Saving is so fast that we have to prevent it from happening in a loop on all frames while
  // user is holding the button.
  boolean readyToSave = true;

  @Override
  public void listen(Input input, int delta) {
    if (KeyboardUtils.isSaveKeyPressed(input)) {
      if (!readyToSave) return;
      try {
        logger.info("Saving to file");
        FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        world.serializeGameData(objectOutputStream);
        logger.info("Done saving to file");
      } catch (IOException e) {
        logger.error("Error saving to file", e);
      }
      readyToSave = false;
    } else {
      readyToSave = true;
    }
  }
}
