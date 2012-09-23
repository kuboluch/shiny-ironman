package kniemkiewicz.jqblocks.ingame.controller.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;

/**
 * User: qba
 * Date: 16.09.12
 */
public class MouseWheelEvent extends InputEvent {
  int delta;

  public MouseWheelEvent(int delta) {
    this.delta = delta;
  }

  public int getDelta() {
    return delta;
  }
}
