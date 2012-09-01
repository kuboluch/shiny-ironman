package kniemkiewicz.jqblocks.ingame.action;

/**
 * User: qba
 * Date: 12.08.12
 */
public class NoInteraction implements Interactive {

  @Override
  public boolean canInteract() {
    return false;
  }

  @Override
  public void interact() { }

  @Override
  public int getActionDuration() {
    return 0;
  }
}
