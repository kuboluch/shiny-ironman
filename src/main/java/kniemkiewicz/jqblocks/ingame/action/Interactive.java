package kniemkiewicz.jqblocks.ingame.action;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Interactive {

  boolean canInteract();

  void interact();

  int getActionDuration();

}
