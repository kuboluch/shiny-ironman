package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.ProjectileController;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.Arrow;
import kniemkiewicz.jqblocks.ingame.content.item.spell.FastTravelItem;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.Button;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.level.LevelGenerator;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
@Component
public class FireballItemController implements ItemController<FireballItem> {

  @Autowired
  PlayerController playerController;

  @Autowired
  ControllerUtils controllerUtils;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  ProjectileController projectileController;

  @Resource
  Sound fireballSound;

  @Autowired
  SoundController soundController;

  @Override
  public void listen(FireballItem selectedItem, List<Event> events) {
    for (MousePressedEvent ev : Collections3.collect(events, MousePressedEvent.class)) {
      if (ev.getButton() == Button.LEFT) {
        generateFireball();
      }
    }
  }

  private void generateFireball() {
    Vector2f pos = new Vector2f(playerController.getPlayer().getShape().getCenterX(), playerController.getPlayer().getShape().getCenterY());
    Vector2f speed = controllerUtils.getCurrentDirection(Fireball.SPEED, new Vector2f(pointOfView.getWindowWidth() / 2, pointOfView.getWindowHeight() / 2));
    projectileController.add(new Fireball(pos.getX(), pos.getY(), playerController.getPlayer(), speed.getX(), speed.getY()));
    soundController.play(fireballSound);
  }

  @Override
  public DroppableObject getObject(FireballItem item, int centerX, int centerY) {
    return null;
  }
}
