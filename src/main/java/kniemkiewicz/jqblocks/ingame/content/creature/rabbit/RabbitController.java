package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.creature.bird.Bird;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.Zombie;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.OncePerXMilliseconds;
import kniemkiewicz.jqblocks.ingame.util.OnceXTimes;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 22.09.12
 */
@Component
public class RabbitController implements UpdateQueue.UpdateController<Rabbit>, HealthController<Rabbit> {
  @Autowired
  World world;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  ControllerUtils utils;

  @Autowired
  AIUtils aiUtils;

  @Override
  public void killed(Rabbit rabbit, QuadTree.HasShape source) {
    world.killMovingObject(rabbit);
  }

  @Override
  public void damaged(Rabbit rabbit, QuadTree.HasShape source, int amount) {
  }

  private OncePerXMilliseconds<Rabbit> tryJumpClosure =
      new OncePerXMilliseconds<Rabbit>(1500, true, new OncePerXMilliseconds.Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      if (utils.isFlying(rabbit.getShape())) return;
      rabbit.getXYMovement().setXSpeed(Player.JUMP_SPEED * 1.2f);
      rabbit.getXYMovement().setYSpeed(-Player.JUMP_SPEED * 0.7f);
    }
  });

  @Override
  public void update(Rabbit rabbit, int delta) {
    if (!utils.isFlying(rabbit.getShape())) {
      rabbit.getXYMovement().setXSpeed(0);
      tryJumpClosure.maybeRunWith(rabbit, delta);
    }
    freeFallController.updateComplex(delta, null, rabbit);
    rabbit.getXYMovement().update(delta);
    rabbit.updateShape();
  }
}
