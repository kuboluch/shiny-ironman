package kniemkiewicz.jqblocks.ingame.event.input.mouse;

public class MouseDraggedEvent extends MouseMovedEvent {
  public MouseDraggedEvent(int oldLevelX, int oldLevelY, int oldScreenX, int oldScreenY, int newLevelX, int newLevelY, int newScreenX, int newScreenY) {
    super(oldLevelX, oldLevelY, oldScreenX, oldScreenY, newLevelX, newLevelY, newScreenX, newScreenY);
  }
}
