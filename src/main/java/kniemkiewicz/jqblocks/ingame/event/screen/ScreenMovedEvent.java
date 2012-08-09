package kniemkiewicz.jqblocks.ingame.event.screen;

import kniemkiewicz.jqblocks.ingame.event.EventBase;

public class ScreenMovedEvent extends EventBase {
  private int oldShiftX;
  private int oldShiftY;
  private int newShiftX;
  private int newShiftY;

  public ScreenMovedEvent(int oldShiftX, int oldShiftY, int newShiftX, int newShiftY) {
    this.oldShiftX = oldShiftX;
    this.oldShiftY = oldShiftY;
    this.newShiftX = newShiftX;
    this.newShiftY = newShiftY;
  }

  public int getOldShiftX() {
    return oldShiftX;
  }

  public int getOldShiftY() {
    return oldShiftY;
  }

  public int getNewShiftX() {
    return newShiftX;
  }

  public int getNewShiftY() {
    return newShiftY;
  }
}
