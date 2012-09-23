package kniemkiewicz.jqblocks.ingame.controller.event;

/**
 * User: qba
 * Date: 09.08.12
 */
public abstract class AbstractEvent implements Event {

  boolean consumed = false;

  @Override
  public boolean isConsumed() {
    return consumed;
  }

  @Override
  public void consume() {
    consumed = true;
  }
}
