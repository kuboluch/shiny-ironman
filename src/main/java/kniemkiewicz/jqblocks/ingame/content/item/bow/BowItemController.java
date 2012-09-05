package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.ArrowController;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.geom.Rectangle;
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

  @Autowired
  PointOfView pointOfView;

  @Autowired
  EventBus eventBus;

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
    for (MousePressedEvent event : pressedEvents) {
      if (event.getButton().equals(Button.LEFT)) {
        shotArrow();
        return;
      }
    }
  }

  // Returns pair dx,dy of vector with given radius, pointing where the bow should be pointed.
  public Pair<Float, Float> getCurrentDirection(float radius) {
    boolean leftFaced = playerController.getPlayer().isLeftFaced();
    Pair<Float, Float> pos = getScreenBowPosition();
    float x0 = pos.getFirst();
    float y0 = pos.getSecond();
    float mouseX = eventBus.getLatestMouseMovedEvent().getNewScreenX();
    float mouseY = eventBus.getLatestMouseMovedEvent().getNewScreenY();
    float dist = (float) Math.sqrt((mouseX - x0) * (mouseX - x0) + (mouseY - y0) * (mouseY - y0));
    float dx;
    float dy;
    if (dist == 0) {
      dx = leftFaced ? - radius : radius;
      dy = 0;
    } else {
      dx = radius * (mouseX - x0) / dist;
      dy = radius * (mouseY - y0) / dist;
    }
    if (((dx > 0) && leftFaced) || (dx < 0 && !leftFaced)) {
      if (dy > 0) {
        dx = 0;
        dy = radius;
      } else {
        dx = 0;
        dy = - radius;
      }
    }
    return Pair.newInstance(dx, dy);
  }

  public Pair<Float, Float> getLevelBowPosition() {
    Rectangle shape = playerController.getPlayer().getShape();
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return Pair.newInstance(shape.getCenterX() + dx, shape.getCenterY() - Sizes.BLOCK / 2);
  }

  public Pair<Float, Float> getScreenBowPosition() {
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return Pair.newInstance((float)pointOfView.getWindowWidth()/ 2 + dx, (float)pointOfView.getWindowHeight() / 2 - Sizes.BLOCK / 2);
  }

  private void shotArrow() {
    Pair<Float, Float> pos = getLevelBowPosition();
    Pair<Float, Float> speed = getCurrentDirection(SPEED);
    arrowController.add(new Arrow(pos.getFirst(), pos.getSecond(), playerController.getPlayer(), speed.getFirst(), speed.getSecond()));
  }
}
