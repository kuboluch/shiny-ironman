package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.ProjectileController;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
  ProjectileController projectileController;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  EventBus eventBus;

  @Resource
  Sound bowSound;

  @Autowired
  SoundController soundController;

  @Autowired
  ControllerUtils controllerUtils;

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

  public Vector2f getLevelBowPosition() {
    Rectangle shape = playerController.getPlayer().getShape();
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return new Vector2f(shape.getCenterX() + dx, shape.getCenterY() - Sizes.BLOCK / 2);
  }

  public Vector2f getScreenBowPosition() {
    float dx = -Sizes.BLOCK / 2;
    if (playerController.getPlayer().isLeftFaced()) {
      dx *= -1;
    }
    return new Vector2f(pointOfView.getWindowWidth()/ 2 + dx, pointOfView.getWindowHeight() / 2 - Sizes.BLOCK / 2);
  }

  private void shotArrow() {
    Vector2f pos = getLevelBowPosition();
    Vector2f speed = controllerUtils.getCurrentDirection(SPEED, getScreenBowPosition());

    projectileController.add(new Arrow(pos.getX(), pos.getY(), playerController.getPlayer(), speed.getX(), speed.getY()));
    soundController.play(bowSound, 0.7f);
  }
}
