package kniemkiewicz.jqblocks.ingame.controller;

import org.newdawn.slick.Input;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class KeyboardUtils {

  static boolean isDownPressed(Input input) {
    return (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN));
  }
  public static boolean isUpPressed(Input input) {
    return (input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP));
  }
  public static boolean isLeftPressed(Input input) {
    return (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT));
  }
  public static boolean isRightPressed(Input input) {
    return (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT));
  }
  public static boolean isExitKeyPressed(Input input) {
    return (input.isKeyDown(Input.KEY_Q) || input.isKeyDown(Input.KEY_ESCAPE));
  }
  public static boolean isRestartKeyPressed(Input input) {
    return (input.isKeyDown(Input.KEY_R));
  }

  /**
   * @return 0-9 for keys and -1 if none is selected
   */
  public static int getPressedNumericKey(Input input) {
    for (int k = Input.KEY_1; k < Input.KEY_9; k++) {
      if (input.isKeyDown(k)) {
        return k - Input.KEY_1 + 1;
      }
    }
    if (input.isKeyPressed(Input.KEY_0)) {
      return 0;
    }
    return -1;
  }
}
