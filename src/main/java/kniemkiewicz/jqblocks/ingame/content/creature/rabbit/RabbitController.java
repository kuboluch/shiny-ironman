package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.creature.FlipImageBody;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.*;
import kniemkiewicz.jqblocks.ingame.controller.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.closure.OncePerXByDistribution;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
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
  ProjectileController projectileController;

  @Autowired
  ControllerUtils utils;

  @Autowired
  AIUtils aiUtils;

  @Autowired
  PlayerController playerController;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  ControllerUtils controllerUtils;

  @Autowired
  UpdateQueue updateQueue;

  static Random random = new Random();

  static final Log logger = LogFactory.getLog(RabbitController.class);

  @Override
  public void killed(Rabbit rabbit, QuadTree.HasShape source) {
    world.killMovingObject(rabbit);
    FlipImageBody body = new FlipImageBody(rabbit.getXYMovement(), RabbitDefinition.WIDTH, RabbitDefinition.HEIGHT, RabbitDefinition.STANDING_RENDERER, RabbitDefinition.STANDING_IMAGE_CENTER_SHIFT);
    body.addTo(renderQueue, freeFallController, updateQueue);
    controllerUtils.pushBodyFrom(body, source, ControllerUtils.DEFAULT_PUSH_BACK);
  }

  @Override
  public void damaged(Rabbit rabbit, QuadTree.HasShape source, int amount) { }

  @Override
  public void update(Rabbit rabbit, int delta) {
    checkForProjectile(rabbit);
    updateState(rabbit, delta);
    executeBehavior(rabbit, delta);
    movementUpdate(rabbit, delta);
  }

  private void movementUpdate(Rabbit rabbit, int delta) {
    if (!utils.isFlying(rabbit.getShape())) {
      // When rabbit is still or falling it means he has landed
      if (rabbit.getXYMovement().getYMovement().getSpeed() >= 0) {
        rabbit.getXYMovement().setXSpeed(0);
      }
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

  private void updateState(Rabbit rabbit, int delta) {
    double distanceToPlayer = getDistanceToPlayer(rabbit);

    if (rabbit.hasProjectileSpotted()) {
      reactToProjectile.maybeRunWith(rabbit, delta);
    }

    switch (rabbit.getState()) {
      case STILL:
        if (distanceToPlayer > DANGER_DISTANCE) {
          startMoving.maybeRunWith(rabbit, delta);
        } else {
          logger.debug(rabbit.getState().name() + " -> " + Rabbit.State.RUNNING.name());
          rabbit.setState(Rabbit.State.RUNNING);
        }
        break;
      case MOVING:
        if (distanceToPlayer > CAUTIOUS_DISTANCE) {
          stopMoving.maybeRunWith(rabbit, delta);
        } else if (distanceToPlayer > DANGER_DISTANCE) {
          if (hasDirectionTowardsPlayer(rabbit)) {
            logger.debug(rabbit.getState().name() + " -> " + Rabbit.State.STILL.name());
            rabbit.setState(Rabbit.State.STILL);
          }
        } else {
          logger.debug(rabbit.getState().name() + " -> " + Rabbit.State.RUNNING.name());
          rabbit.setState(Rabbit.State.RUNNING);
        }
        break;
      case RUNNING:
        Rabbit.State newState = null;
        if (distanceToPlayer > CAUTIOUS_DISTANCE) {
          if (random.nextInt(100) < 50) {
            newState = Rabbit.State.STILL;
          } else {
            newState = Rabbit.State.MOVING;
          }
        } else if (distanceToPlayer > DANGER_DISTANCE) {
          if (hasDirectionTowardsPlayer(rabbit)) {
            newState = Rabbit.State.STILL;
          } else {
            newState = Rabbit.State.MOVING;
          }
        }
        if (newState != null) {
          logger.debug(rabbit.getState().name() + " -> " + newState.name());
          rabbit.setState(newState);
        }
        break;
      case RUNNING_AWAY:
        stopRunningAway.maybeRunWith(rabbit, delta);
        break;
    }
  }

  private void executeBehavior(Rabbit rabbit, int delta) {
    double distanceToPlayer = getDistanceToPlayer(rabbit);
    boolean duringJump = utils.isFlying(rabbit.getShape());

    switch (rabbit.getState()) {
      case STILL:
        break;
      case MOVING:
        if (hasDirectionTowardsPlayer(rabbit) && distanceToPlayer < CAUTIOUS_DISTANCE) {
          changeDirection(rabbit);
        } else {
          changeDirection.maybeRunWith(rabbit, delta);
        }
        if (!duringJump) {
          movingJump.maybeRunWith(rabbit, delta);
        }
        break;
      case RUNNING:
      case RUNNING_AWAY:
        if (hasDirectionTowardsPlayer(rabbit)) {
          changeDirection(rabbit);
        }
        if (!duringJump) {
          runningJump.maybeRunWith(rabbit, delta);
        }
        break;
    }
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

  OncePerXByDistribution<Rabbit> movingJump = new OncePerXByDistribution<Rabbit>(MOVING_JUMPS, jumpClosure);

  OncePerXByDistribution<Rabbit> runningJump = new OncePerXByDistribution<Rabbit>(RUNNING_JUMPS, jumpClosure);

  OncePerXByDistribution<Rabbit> changeDirection = new OncePerXByDistribution<Rabbit>(CHANGE_DIRECTION, new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      changeDirection(rabbit);
      logger.debug("CHANGE_DIRECTION");
    }
  });

  OncePerXByDistribution<Rabbit> startMoving = new OncePerXByDistribution<Rabbit>(START_MOVING, new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      rabbit.setState(Rabbit.State.MOVING);
      logger.debug("START_MOVING");
    }
  });

  OncePerXByDistribution<Rabbit> stopMoving = new OncePerXByDistribution<Rabbit>(STOP_MOVING, new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      logger.debug("STOP_MOVING");
      rabbit.setState(Rabbit.State.STILL);
    }
  });

  private void checkForProjectile(Rabbit rabbit) {
    Shape awarenessRangeShape = new Circle(rabbit.getShape().getCenterX(), rabbit.getShape().getCenterY(), AWARENESS_RADIUS, 8);
    for (ProjectileController.Projectile projectile : projectileController.getProjectiles()) {
      if (GeometryUtils.intersects(awarenessRangeShape, projectile.getShape())) {
        rabbit.setProjectileSpotted(true);
      }
    }
  }

  OncePerXByDistribution<Rabbit> reactToProjectile = new OncePerXByDistribution<Rabbit>(REACTION_TIME, new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      logger.debug(rabbit.getState().name() + " -> " + Rabbit.State.RUNNING_AWAY.name());
      rabbit.setState(Rabbit.State.RUNNING_AWAY);
      stopRunningAway.reset(rabbit);
      rabbit.setProjectileSpotted(false);
    }
  });

  OncePerXByDistribution<Rabbit> stopRunningAway = new OncePerXByDistribution<Rabbit>(STOP_RUNNING_AWAY, new Closure<Rabbit>() {
    @Override
    public void run(Rabbit rabbit) {
      logger.debug(rabbit.getState().name() + " -> " + Rabbit.State.MOVING.name());
      rabbit.setState(Rabbit.State.MOVING);
    }
  });

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