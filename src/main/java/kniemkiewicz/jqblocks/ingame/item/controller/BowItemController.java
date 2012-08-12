package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.item.BowItem;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.object.arrow.ArrowController;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Shape;
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
  PlayerController playerController;

  @Autowired
  ArrowController arrowController;

  private static float SPEED = Sizes.MAX_FALL_SPEED / 1.5f;

  @Override
  public void listen(BowItem bowItem, List<Event> events) {
    List<MousePressedEvent> pressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!pressedEvents.isEmpty()) {
      handlePressedEvent(pressedEvents);
    }
  }

  @Override
  public Shape getDropObjectShape(BowItem item, int centerX, int centerY) {
    return null;
  }

  @Override
  public MovingPhysicalObject getDropObject(BowItem item, int centerX, int centerY) {
    return null;
  }

  private void handlePressedEvent(List<MousePressedEvent> pressedEvents) {
    assert pressedEvents.size() > 0;
    MousePressedEvent pe = pressedEvents.get(0);
    shotArrow(pe.getLevelX(), pe.getLevelY());
  }

  private void shotArrow(int levelX, int levelY) {
    Player player = playerController.getPlayer();
    int dx = (int)(levelX - player.getXMovement().getPos());
    int dy = (int)(levelY - player.getYMovement().getPos());
    if ((dx == 0) && (dy == 0)) return;
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float vx = dx / dd * SPEED;
    float vy = dy / dd * SPEED;
    float x = player.getShape().getCenterX();
    float y = player.getYMovement().getPos() + Player.HEIGHT / 3;
    arrowController.add(new Arrow(x, y, player, vx, vy));
  }
}
