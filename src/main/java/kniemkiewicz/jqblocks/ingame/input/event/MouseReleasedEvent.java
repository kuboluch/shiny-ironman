package kniemkiewicz.jqblocks.ingame.input.event;

public class MouseReleasedEvent extends MousePressedEvent {
  public MouseReleasedEvent(int button, int levelX, int levelY, int screenX, int screenY) {
    super(button, levelX, levelY, screenX, screenY);
  }
}
