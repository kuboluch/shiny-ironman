package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.MouseClickListener;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ArrowController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.object.Arrow;
import kniemkiewicz.jqblocks.ingame.object.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class BowItemController implements ItemController {

  @Autowired
  Player player;

  @Autowired
  ArrowController arrowController;

  private static float SPEED = Sizes.MAX_FALL_SPEED / 1.5f;

  @Override
  public void listen(List<Click> clicks) {
    assert clicks.size() > 0;
    Click c = clicks.get(0);
    int dx = (int)(c.getX() - player.getXMovement().getPos());
    int dy = (int)(c.getY() - player.getYMovement().getPos());
    if ((dx == 0) && (dy == 0)) return;
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float vx = dx / dd * SPEED;
    float vy = dy / dd * SPEED;
    float x = player.getShape().getCenterX();
    float y = player.getYMovement().getPos() + Player.HEIGHT / 3;
    arrowController.add(new Arrow(x, y, player, vx, vy));
  }
}
