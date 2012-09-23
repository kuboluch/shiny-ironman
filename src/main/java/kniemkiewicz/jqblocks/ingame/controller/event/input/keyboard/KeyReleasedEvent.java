package kniemkiewicz.jqblocks.ingame.controller.event.input.keyboard;

import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;

/**
 * User: qba
 * Date: 12.08.12
 */
public class KeyReleasedEvent extends InputEvent {
  // The key code that was pressed (@see org.newdawn.slick.Input)
  int key;
  // The character of the key that was pressed
  char c;

  public KeyReleasedEvent(int key, char c) {
    this.key = key;
    this.c = c;
  }

  public int getKey() {
    return key;
  }

  public char getC() {
    return c;
  }
}
