package kniemkiewicz.jqblocks.ingame.event.input.mouse;

/**
 * User: knie
 * Date: 7/27/12
 */
public enum Button {
  LEFT,
  RIGHT,
  UNKNOWN;
  static Button parseInt(int b) {
    switch (b) {
      case 0: return LEFT;
      case 1: return RIGHT;
      default: return UNKNOWN;
    }
  }

}
