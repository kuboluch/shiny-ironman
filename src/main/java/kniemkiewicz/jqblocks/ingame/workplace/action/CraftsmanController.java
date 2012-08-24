package kniemkiewicz.jqblocks.ingame.workplace.action;

import kniemkiewicz.jqblocks.ingame.action.Interactive;

/**
 * User: qba
 * Date: 16.08.12
 */
public class CraftsmanController implements Interactive {

  private static final int CRAFTING_DURATION = 1000;

  @Override
  public boolean canInteract() {
    return true;
  }

  @Override
  public void interact() {

  }

  @Override
  public int getDurationToComplete() {
    return CRAFTING_DURATION;
  }
}
