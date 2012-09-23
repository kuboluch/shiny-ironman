package kniemkiewicz.jqblocks.ingame.controller.action;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Interactive {

  boolean canInteract();

  void interact();

  int getActionDuration();

}
