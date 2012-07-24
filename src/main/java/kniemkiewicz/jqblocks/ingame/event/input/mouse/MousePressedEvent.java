package kniemkiewicz.jqblocks.ingame.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;

public class MousePressedEvent extends InputEvent {
  private int button;
  private int levelX;
  private int levelY;
  private int screenX;
  private int screenY;

  public MousePressedEvent(int button, int levelX, int levelY, int screenX, int screenY) {
    super();
    this.button = button;
    this.levelX = levelX;
    this.levelY = levelY;
    this.screenX = screenX;
    this.screenY = screenY;
  }

  public int getButton() {
    return button;
  }

  public int getLevelX() {
    return levelX;
  }

  public int getLevelY() {
    return levelY;
  }

  public int getScreenX() {
    return screenX;
  }

  public int getScreenY() {
    return screenY;
  }
}
