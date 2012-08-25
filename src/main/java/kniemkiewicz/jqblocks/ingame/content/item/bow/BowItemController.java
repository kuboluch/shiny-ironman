package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.ArrowController;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
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
  public DroppableObject getObject(BowItem item, int centerX, int centerY) {
    return null;
  }

  private void handlePressedEvent(List<MousePressedEvent> pressedEvents) {
    assert pressedEvents.size() > 0;
    MousePressedEvent pe = pressedEvents.get(0);
    shotArrow(pe.getLevelX(), pe.getLevelY());
  }

  private void shotArrow(int levelX, int levelY) {
    Player player = playerController.getPlayer();
    int dx = (int)(levelX - player.getFullXYMovement().getX());
    int dy = (int)(levelY - player.getFullXYMovement().getY());
    if ((dx == 0) && (dy == 0)) return;
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float vx = dx / dd * SPEED;
    float vy = dy / dd * SPEED;
    float x = player.getShape().getCenterX();
    float y = player.getFullXYMovement().getY() + Player.HEIGHT / 3;
    arrowController.add(new Arrow(x, y, player, vx, vy));
  }
}