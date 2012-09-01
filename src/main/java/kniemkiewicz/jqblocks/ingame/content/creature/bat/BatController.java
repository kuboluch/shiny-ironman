package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.ai.AIUtils;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.HasSource;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.OnceXTimes;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: knie
 * Date: 7/25/12
 */
@Component
public class BatController implements UpdateQueue.UpdateController<Bat>, HealthController<Bat> {

  static final Log logger = LogFactory.getLog(BatController.class);

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World world;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  ControllerUtils utils;

  @Autowired
  AIUtils aiUtils;

  public static int BITE_DMG = 20;

  private OnceXTimes<Bat> findEnemyClosure = new OnceXTimes<Bat>(30, true, new OnceXTimes.Closure<Bat>() {
    @Override
    public void run(Bat bat) {
      if (bat.getTarget() != null) {
        if (bat.getTarget() instanceof HasHealthPoints) {
          if (((HasHealthPoints) bat.getTarget()).getHp().getCurrentHp() == 0) {
            bat.setAccelerationVector(null);
            bat.setTarget(null);
          } else {
            return;
          }
        }
      }
      bat.setTarget(aiUtils.findNearbyVillager(bat, Sizes.BLOCK * 10));
    }
  });

  private OnceXTimes<Bat> calculateAccelerationVectorClosure = new OnceXTimes<Bat>(30, true, new OnceXTimes.Closure<Bat>() {
    @Override
    public void run(Bat bat) {
      assert bat.getTarget() != null;
      bat.setAccelerationVector(utils.getVectorScaled(bat.getShape().getCenterX(), bat.getShape().getCenterY(),
          bat.getTarget().getShape().getCenterX(), bat.getTarget().getShape().getCenterY(),
          Bat.ACCELERATION));
    }
  });

  @Override
  public void update(Bat bat, int delta) {
    findEnemyClosure.maybeRunWith(bat);
    if (bat.getTarget() == null) {
      List<PhysicalObject> collisions = new ArrayList<PhysicalObject>();
      aiUtils.horizontalPatrol(bat, delta, collisions);
      for (PhysicalObject p : collisions) {
        ((HasHealthPoints) p).getHp().damageRateLimited(bat, BITE_DMG, 300, world);
      }
    } else {
      calculateAccelerationVectorClosure.maybeRunWith(bat);
      if (bat.getAccelerationVector() != null) {
        bat.getXYMovement().applyAcceleration(bat.getAccelerationVector());
      }
      bat.getXYMovement().update(delta);
      bat.updateShape();
      utils.resolveCollisionsWithWalls(bat);
      utils.damageTouchedVillagers(bat, BITE_DMG);
    }
  }

  @Override
  public void killed(Bat object, QuadTree.HasShape source) {
    world.killMovingObject(object);
    float x;
    if (source instanceof HasSource) {
      x = ((HasSource) source).getSource().getShape().getCenterX();
    } else {
      x = source.getShape().getCenterX();
    }
    BatBody body = new BatBody(object.getXYMovement().getX(), object.getXYMovement().getY(),
        (x <  object.getXYMovement().getX()) ? 1 : -1);
    body.addTo(renderQueue, freeFallController);
  }

  @Override
  public void damaged(Bat object, QuadTree.HasShape source, int amount) { }
}
