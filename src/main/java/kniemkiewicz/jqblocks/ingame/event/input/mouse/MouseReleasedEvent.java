package kniemkiewicz.jqblocks.ingame.event.input.mouse;

public class MouseReleasedEvent extends MousePressedEvent {
  public MouseReleasedEvent(Button button, int levelX, int levelY, int screenX, int screenY) {
    super(button, levelX, levelY, screenX, screenY);
  }
}
