package kniemkiewicz.jqblocks.ingame.content.creature.rooster;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.Enemy;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.closure.OncePerXByDistribution;
import kniemkiewicz.jqblocks.ingame.util.closure.OnceXTimes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static kniemkiewicz.jqblocks.ingame.content.creature.rooster.RoosterDefinition.*;

/**
 * User: qba
 * Date: 08.10.12
 */
@Component
public class RoosterController implements UpdateQueue.UpdateController<Rooster>, HealthController<Rooster> {
  @Autowired
  World world;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  ControllerUtils utils;

  @Autowired
  AIUtils aiUtils;

  @Autowired
  SolidBlocks blocks;

  static final Log logger = LogFactory.getLog(RoosterController.class);

  @Override
  public void killed(Rooster rooster, QuadTree.HasShape source) {
    world.killMovingObject(rooster);
  }

  @Override
  public void damaged(Rooster rooster, QuadTree.HasShape source, int amount) {
  }

  @Override
  public void update(Rooster rooster, int delta) {
    findEnemyClosure.maybeRunWith(rooster);
    updateState(rooster, delta);
    executeBehavior(rooster, delta);
    movementUpdate(rooster, delta);
    rooster.setAge(rooster.getAge() + delta);
  }
  
  private void movementUpdate(Rooster rooster, int delta) {
    freeFallController.updateComplex(Math.round(delta), null, rooster);
  }
  
  private void updateState(Rooster rooster, int delta) {
    switch (rooster.getState()) {
      case STILL:
        if (rooster.getEnemySpotted() != null) {
          rooster.setState(Rooster.State.RUNNING_AWAY);
        } else {
          startMoving.maybeRunWith(rooster, delta);
        }
        break;
      case MOVING:
        if (rooster.getEnemySpotted() != null) {
          rooster.setState(Rooster.State.RUNNING_AWAY);
        } else {
          stopMoving.maybeRunWith(rooster, delta);
        }
        break;
      case RUNNING_AWAY:
        stopRunningAway.maybeRunWith(rooster, delta);
        break;
    }
  }

  private void executeBehavior(Rooster rooster, int delta) {
    boolean xDirection = rooster.getXYMovement().getXMovement().getDirection();

    switch (rooster.getState()) {
      case STILL:
        break;
      case MOVING:
        changeDirection.maybeRunWith(rooster, delta);
        if (utils.isFacingWall(rooster.getShape(), xDirection) && utils.isOnSolidGround(rooster.getShape())) {
          if (utils.isFacingWallToStandOn(rooster.getShape(), xDirection, Sizes.BLOCK)) {
            utils.pushFrom(rooster, (xDirection ? -1 : 1), -1, ControllerUtils.DEFAULT_PUSH_BACK);
          } else {
            changeDirection(rooster);
          }
        }
        walk(rooster);
        break;
      case RUNNING_AWAY:
        if (rooster.getEnemySpotted() != null && hasDirectionTowardsEnemy(rooster)) {
          changeDirection(rooster);
        }
        if (utils.isFacingWall(rooster.getShape(), xDirection)) {
          utils.pushFrom(rooster, (xDirection ? -Sizes.BLOCK : Sizes.BLOCK), 0, ControllerUtils.DEFAULT_PUSH_BACK / 4);
        }
        run(rooster);
        break;
    }
  }

  private void walk(Rooster rooster) {
    if (rooster.getXYMovement().getXMovement().getDirection()) {
      if (rooster.getXYMovement().getXMovement().getSpeed() > WALK_SPEED) {
        rooster.getXYMovement().getXMovement().acceleratePositive();
      } else {
        rooster.getXYMovement().getXMovement().setSpeed(WALK_SPEED);
      }
    } else {
      if (rooster.getXYMovement().getXMovement().getSpeed() < -WALK_SPEED) {
        rooster.getXYMovement().getXMovement().accelerateNegative();
      } else {
        rooster.getXYMovement().getXMovement().setSpeed(-WALK_SPEED);
      }
    }
  }

  private void run(Rooster rooster) {
    if (rooster.getXYMovement().getXMovement().getDirection()) {
      rooster.getXYMovement().getXMovement().acceleratePositive();
    } else {
      rooster.getXYMovement().getXMovement().accelerateNegative();
    }
  }

  private OnceXTimes<Rooster> findEnemyClosure = new OnceXTimes<Rooster>(30, true, new Closure<Rooster>() {
    @Override
    public void run(Rooster rooster) {
      Enemy enemyNearby = aiUtils.findNearbyEnemy(rooster, AWARENESS_RADIUS);
      rooster.setEnemySpotted(enemyNearby);
    }
  });
  
   OncePerXByDistribution<Rooster> changeDirection = new OncePerXByDistribution<Rooster>(CHANGE_DIRECTION, new Closure<Rooster>() {
    @Override
    public void run(Rooster rooster) {
      changeDirection(rooster);
      logger.debug("CHANGE_DIRECTION");
    }
  });

  OncePerXByDistribution<Rooster> startMoving = new OncePerXByDistribution<Rooster>(START_MOVING, new Closure<Rooster>() {
    @Override
    public void run(Rooster rooster) {
      rooster.setState(Rooster.State.MOVING);
      logger.debug("START_MOVING");
    }
  });

  OncePerXByDistribution<Rooster> stopMoving = new OncePerXByDistribution<Rooster>(STOP_MOVING, new Closure<Rooster>() {
    @Override
    public void run(Rooster rooster) {
      logger.debug("STOP_MOVING");
      rooster.setState(Rooster.State.STILL);
    }
  });

  OncePerXByDistribution<Rooster> stopRunningAway = new OncePerXByDistribution<Rooster>(STOP_RUNNING_AWAY, new Closure<Rooster>() {
    @Override
    public void run(Rooster rooster) {
      logger.debug(rooster.getState().name() + " -> " + Rooster.State.MOVING.name());
      rooster.setState(Rooster.State.MOVING);
    }
  });

  private void changeDirection(Rooster rooster) {
    rooster.getXYMovement().getXMovement().setDirection(!rooster.getXYMovement().getXMovement().getDirection());
  }

  private boolean hasDirectionTowardsEnemy(Rooster rooster) {
    if (rooster.getEnemySpotted().getShape().getCenterX() >= rooster.getShape().getCenterX()) {
      return rooster.getXYMovement().getXMovement().getDirection();
    } else {
      return !rooster.getXYMovement().getXMovement().getDirection();
    }
  }
}
