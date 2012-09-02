package kniemkiewicz.jqblocks.ingame.production.action;

import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.production.CanProduce;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 02.09.12
 */
@Component
public class PlayerProductionActionController extends ProductionActionController {

  @Autowired
  PlayerController playerController;

  @Override
  public CanProduce getProductionPlace(Rectangle rectangle) {
    if (GeometryUtils.intersects(playerController.getPlayer().getShape(), rectangle)) {
      return playerController.getPlayer();
    }
    return null;
  }
}
