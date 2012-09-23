package kniemkiewicz.jqblocks.ingame.controller.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;

public class MouseReleasedEvent extends InputEvent {
  private Button button;
  private int levelX;
  private int levelY;
  private int screenX;
  private int screenY;

  public MouseReleasedEvent(Button button, int levelX, int levelY, int screenX, int screenY) {
    super();
    this.button = button;
    this.levelX = levelX;
    this.levelY = levelY;
    this.screenX = screenX;
    this.screenY = screenY;
  }

  public Button getButton() {
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
