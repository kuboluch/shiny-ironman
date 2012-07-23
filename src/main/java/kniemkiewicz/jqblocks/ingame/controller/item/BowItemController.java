package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ArrowController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.object.Arrow;
import kniemkiewicz.jqblocks.ingame.object.Player;
import kniemkiewicz.jqblocks.util.Collections3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
  public void listen(List<InputEvent> events) {
    List<MouseClickEvent> clickEvents = Collections3.collect(events, MouseClickEvent.class);

    if (!clickEvents.isEmpty()) {
      handleClickEvent(clickEvents);
    }
  }

  private void handleClickEvent(List<MouseClickEvent> clickEvents) {
    assert clickEvents.size() > 0;
    MouseClickEvent ce = clickEvents.get(0);
    int dx = (int)(ce.getLevelX() - player.getXMovement().getPos());
    int dy = (int)(ce.getLevelY() - player.getYMovement().getPos());
    if ((dx == 0) && (dy == 0)) return;
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float vx = dx / dd * SPEED;
    float vy = dy / dd * SPEED;
    float x = player.getShape().getCenterX();
    float y = player.getYMovement().getPos() + Player.HEIGHT / 3;
    arrowController.add(new Arrow(x, y, player, vx, vy));
  }
}
