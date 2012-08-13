package kniemkiewicz.jqblocks.ingame.action;

/**
 * User: qba
 * Date: 12.08.12
 */
public class NullActionController implements Interactive {

  @Override
  public boolean canInteract() {
    return false;
  }

  @Override
  public void interact() { }

  @Override
  public int getDurationToComplete() {
    return 0;
  }
}
