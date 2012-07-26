package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.arrow.ArrowController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.item.BowItem;
import kniemkiewicz.jqblocks.ingame.object.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.util.Collections3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 7/21/12
 */
@Component
public class BowItemController implements ItemController<BowItem> {

  @Autowired
  Player player;

  @Autowired
  ArrowController arrowController;

  private static float SPEED = Sizes.MAX_FALL_SPEED / 1.5f;

  @Override
  public void listen(BowItem bowItem, List<Event> events) {
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
