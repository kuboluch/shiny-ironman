package kniemkiewicz.jqblocks.ingame.controller.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;

public class MouseMovedEvent extends InputEvent {
  private int oldLevelX;
  private int oldLevelY;
  private int oldScreenX;
  private int oldScreenY;
  private int newLevelX;
  private int newLevelY;
  private int newScreenX;
  private int newScreenY;

  public MouseMovedEvent(int oldLevelX, int oldLevelY, int oldScreenX, int oldScreenY,
                         int newLevelX, int newLevelY, int newScreenX, int newScreenY) {
    super();
    this.oldLevelX = oldLevelX;
    this.oldLevelY = oldLevelY;
    this.oldScreenX = oldScreenX;
    this.oldScreenY = oldScreenY;
    this.newLevelX = newLevelX;
    this.newLevelY = newLevelY;
    this.newScreenX = newScreenX;
    this.newScreenY = newScreenY;
  }

  public int getOldLevelX() {
    return oldLevelX;
  }

  public int getOldLevelY() {
    return oldLevelY;
  }

  public int getOldScreenX() {
    return oldScreenX;
  }

  public int getOldScreenY() {
    return oldScreenY;
  }

  public int getNewLevelX() {
    return newLevelX;
  }

  public int getNewLevelY() {
    return newLevelY;
  }

  public int getNewScreenX() {
    return newScreenX;
  }

  public int getNewScreenY() {
    return newScreenY;
  }
}
