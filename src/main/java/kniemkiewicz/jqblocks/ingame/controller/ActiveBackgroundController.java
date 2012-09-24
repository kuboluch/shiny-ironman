package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.content.background.Portal;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * User: krzysiek
 * Date: 24.09.12
 */
@Component
public class ActiveBackgroundController {

  @Autowired
  PlayerController playerController;

  @Autowired
  Backgrounds backgrounds;

  public void update() {
    Iterator<BackgroundElement> it = backgrounds.intersects(playerController.getPlayer().getShape());
    while (it.hasNext()) {
      BackgroundElement be = it.next();
      if (be instanceof Portal) {
        handlePortal((Portal)be);
      }
    }
  }

  private void handlePortal(Portal portal) {
    if (portal.getDestination() != null) {
      Player player = playerController.getPlayer();
      player.getXYMovement().getXMovement().setPos(portal.getDestination().getPos().getX());
      player.getXYMovement().getYMovement().setPos(portal.getDestination().getPos().getY());
      player.updateShape();
    }
  }
}
