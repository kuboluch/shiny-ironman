package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.closure.OncePerXByGaussian;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static kniemkiewicz.jqblocks.ingame.content.creature.rabbit.RabbitDefinition.*;

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

  @Autowired
  PlayerController playerController;

  static Random random = new Random();
  
  static final Log logger = LogFactory.getLog(RabbitController.class);

  @Override
  public void killed(Rabbit rabbit, QuadTree.HasShape source) {
    world.killMovingObject(rabbit);
  }

  @Override
  public void damaged(Rabbit rabbit, QuadTree.HasShape source, int amount) {
  }

  Closure<Rabbit> jumpClosure = new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      if (utils.isFlying(rabbit.getShape())) return;
      float direction = rabbit.getXYMovement().getXMovement().getDirection() ? 1 : -1;
      rabbit.getXYMovement().setXSpeed(direction * JUMP_SPEED_X);
      rabbit.getXYMovement().setYSpeed(-JUMP_SPEED_Y);
    }
  };

  private OncePerXByGaussian<Rabbit> movingJump =
      new OncePerXByGaussian<Rabbit>(MOVING_JUMPS, jumpClosure);

  private OncePerXByGaussian<Rabbit> runningJump =
      new OncePerXByGaussian<Rabbit>(RUNNING_JUMPS, jumpClosure);

  @Override
  public void update(Rabbit rabbit, int delta) {
    if (!utils.isFlying(rabbit.getShape())) {
      // When rabbit is still or falling it means he has landed
      if (rabbit.getXYMovement().getYMovement().getSpeed() >= 0) {
        rabbit.getXYMovement().setXSpeed(0);
      }
      // AI
      updateState(rabbit, delta);
      executeBehavior(rabbit, delta);
    } else {
      // During jump
      if (rabbit.getXYMovement().getYMovement().getSpeed() < 0) {
        if (rabbit.getXYMovement().getXMovement().getDirection()) {
          rabbit.getXYMovement().getXMovement().acceleratePositive();
        } else {
          rabbit.getXYMovement().getXMovement().accelerateNegative();
        }
      }
    }
    freeFallController.updateComplex(Math.round(delta / RABBIT_SLOWNESS), null, rabbit);
  }

  private OncePerXByGaussian<Rabbit> changeDirection =
      new OncePerXByGaussian<Rabbit>(CHANGE_DIRECTION, new Closure<Rabbit>() {
        @Override
        public void run(Rabbit rabbit) {
          rabbit.getXYMovement().getXMovement().setDirection(!rabbit.getXYMovement().getXMovement().getDirection());
          logger.debug("CHANGE_DIRECTION");
        }
      });

  private OncePerXByGaussian<Rabbit> startMoving =
      new OncePerXByGaussian<Rabbit>(START_MOVING, new Closure<Rabbit>() {
        @Override
        public void run(Rabbit rabbit) {
          rabbit.setState(Rabbit.State.MOVING);
          logger.debug("START_MOVING");
        }
      });

  private OncePerXByGaussian<Rabbit> stopMoving =
      new OncePerXByGaussian<Rabbit>(STOP_MOVING, new Closure<Rabbit>() {
        @Override
        public void run(Rabbit rabbit) {
          rabbit.setState(Rabbit.State.STILL);
          logger.debug("STOP_MOVING");
        }
      });

  private void updateState(Rabbit rabbit, int delta) {
    double distanceToPlayer = getDistanceToPlayer(rabbit);

    switch (rabbit.getState()) {
      case STILL:
        if (distanceToPlayer > DANGER_DISTANCE) {
          startMoving.maybeRunWith(rabbit, delta);
        } else {
          rabbit.setState(Rabbit.State.RUNNING_AWAY);
          logger.debug("STILL -> RUNNING_AWAY");
        }
        break;
      case MOVING:
        if (distanceToPlayer > CAUTIOUS_DISTANCE) {
          stopMoving.maybeRunWith(rabbit, delta);
        } else if (distanceToPlayer > DANGER_DISTANCE) {
          if (hasDirectionTowardsPlayer(rabbit)) {
            rabbit.setState(Rabbit.State.STILL);
            logger.debug("MOVING -> STILL");
          }
        } else {
          rabbit.setState(Rabbit.State.RUNNING_AWAY);
          logger.debug("MOVING -> RUNNING_AWAY");
        }
        break;
      case RUNNING_AWAY:
        if (distanceToPlayer > CAUTIOUS_DISTANCE) {
          if (random.nextInt(100) < 50) {
            rabbit.setState(Rabbit.State.STILL);
            logger.debug("RUNNING_AWAY -> STILL");
          } else {
            rabbit.setState(Rabbit.State.MOVING);
            logger.debug("RUNNING_AWAY -> MOVING");
          }
        } else if (distanceToPlayer > DANGER_DISTANCE) {
          if (hasDirectionTowardsPlayer(rabbit)) {
            rabbit.setState(Rabbit.State.STILL);
            logger.debug("RUNNING_AWAY -> STILL");
          } else {
            rabbit.setState(Rabbit.State.MOVING);
            logger.debug("RUNNING_AWAY -> MOVING");
          }
        }
        break;
    }
  }

  private void executeBehavior(Rabbit rabbit, int delta) {
    double distanceToPlayer = getDistanceToPlayer(rabbit);

    switch (rabbit.getState()) {
      case STILL:
        break;
      case MOVING:
        if (hasDirectionTowardsPlayer(rabbit) && distanceToPlayer < CAUTIOUS_DISTANCE) {
          changeDirection(rabbit);
        } else {
          changeDirection.maybeRunWith(rabbit, delta);
        }
        movingJump.maybeRunWith(rabbit, delta);
        break;
      case RUNNING_AWAY:
        if (hasDirectionTowardsPlayer(rabbit)) {
          changeDirection(rabbit);
        }
        runningJump.maybeRunWith(rabbit, delta);
        break;
    }
  }

  private void changeDirection(Rabbit rabbit) {
    rabbit.getXYMovement().getXMovement().setDirection(!rabbit.getXYMovement().getXMovement().getDirection());
  }

  private double getDistanceToPlayer(Rabbit rabbit) {
    float rabbitX = rabbit.getShape().getCenterX();
    float rabbitY = rabbit.getShape().getCenterY();
    float playerX = playerController.getPlayer().getShape().getCenterX();
    float playerY = playerController.getPlayer().getShape().getCenterY();
    return GeometryUtils.distance(rabbitX, rabbitY, playerX, playerY);
  }

  private boolean hasDirectionTowardsPlayer(Rabbit rabbit) {
    if (playerController.getPlayer().getShape().getCenterX() >= rabbit.getShape().getCenterX()) {
      return rabbit.getXYMovement().getXMovement().getDirection();
    } else {
      return !rabbit.getXYMovement().getXMovement().getDirection();
    }
  }
}
