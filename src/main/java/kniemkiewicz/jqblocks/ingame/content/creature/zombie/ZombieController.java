package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.util.OnceXTimes;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ZombieController implements HealthController<Zombie>, UpdateQueue.UpdateController<Zombie> {

  @Autowired
  World world;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  ControllerUtils controllerUtils;

  @Autowired
  PlayerController playerController;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  UpdateQueue updateQueue;

  private static float DETECTION_RECT_WIDTH = Sizes.BLOCK * 2;

  private OnceXTimes<Zombie> tryJumpClosure = new OnceXTimes<Zombie>(10, true, new OnceXTimes.Closure<Zombie>() {
    @Override
    public void run(Zombie zombie) {
      if (controllerUtils.isFlying(zombie.getShape())) return;
      Rectangle rect = null;
      if (zombie.isLeftFaced()) {
        // We use max here as precision is of minor importance.
        // this rect is meant to be on the height of zombie legs and stretching in direction of its movement.
        rect = new Rectangle(zombie.getShape().getX() - DETECTION_RECT_WIDTH, zombie.getShape().getMaxY() - 5, DETECTION_RECT_WIDTH, 0);
      }  else {
        rect = new Rectangle(zombie.getShape().getMaxX(), zombie.getShape().getMaxY() - 5, DETECTION_RECT_WIDTH, 0);
      }
      if (solidBlocks.getBlocks().collidesWithNonEmpty(rect)) {
        zombie.getXYMovement().setYSpeed(- Player.JUMP_SPEED / 2);
      }
    }
  });

  private static int TOUCH_DMG = 50;

  @Override
  public void killed(Zombie object, QuadTree.HasShape source) {
    world.killMovingObject(object);
    ZombieBody body = new ZombieBody(object.getXYMovement());
    body.addTo(renderQueue, freeFallController, updateQueue);
    controllerUtils.pushFrom(body, source, ControllerUtils.DEFAULT_PUSH_BACK / 2);
  }

  @Override
  public void damaged(Zombie object, QuadTree.HasShape source, int amount) {
    controllerUtils.pushFrom(object, source, ControllerUtils.DEFAULT_PUSH_BACK / 2);
  }

  @Override
  public void update(Zombie object, int delta) {
    followPlayer(object);
    freeFallController.updateComplex(delta, null, object);
    controllerUtils.damageTouchedVillagers(object, TOUCH_DMG);
  }

  public void followPlayer(Zombie zombie) {
    Player player = playerController.getPlayer();
    if (player.getShape().getCenterX() < zombie.getShape().getCenterX()) {
      zombie.getXYMovement().getXMovement().accelerateNegative();
    } else {
      zombie.getXYMovement().getXMovement().acceleratePositive();
    }
    tryJumpClosure.maybeRunWith(zombie);
  }
}
