package kniemkiewicz.jqblocks.ingame.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.event.input.InputEvent;

public class MouseClickEvent extends InputEvent {
  private int button;
  private int levelX;
  private int levelY;
  private int screenX;
  private int screenY;
  private int clickCount;

  public MouseClickEvent(int button, int levelX, int levelY, int screenX, int screenY, int clickCount) {
    super();
    this.button = button;
    this.levelX = levelX;
    this.levelY = levelY;
    this.screenX = screenX;
    this.screenY = screenY;
    this.clickCount = clickCount;
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

  public int getClickCount() {
    return clickCount;
  }
}