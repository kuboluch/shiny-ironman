package kniemkiewicz.jqblocks.ingame.controller.event.input.mouse;

public class MouseDraggedEvent extends MouseMovedEvent {

  private final Button  button;

  public MouseDraggedEvent(int oldLevelX, int oldLevelY, int oldScreenX, int oldScreenY, int newLevelX, int newLevelY, int newScreenX, int newScreenY, Button button) {
    super(oldLevelX, oldLevelY, oldScreenX, oldScreenY, newLevelX, newLevelY, newScreenX, newScreenY);
    this.button = button;
  }

  public Button getButton() {
    return button;
  }
}
