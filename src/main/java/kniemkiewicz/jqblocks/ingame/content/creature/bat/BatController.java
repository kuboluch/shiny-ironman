package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.HasSource;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/25/12
 */
@Component
public class BatController implements UpdateQueue.UpdateController<Bat>, HealthController<Bat> {

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

  public static int BITE_DMG = 20;

  @Override
  public void update(Bat bat, int delta) {
    float prevX = bat.getMovement().getX();
    bat.getMovement().update(delta);
    bat.rectangle.setX(bat.getMovement().getX());
    if (hits(bat)) {
      SingleAxisMovement xMovement = bat.getMovement().getXMovement();
      xMovement.setPos(prevX);
      xMovement.setSpeed(- xMovement.getSpeed());
      bat.rectangle.setX(xMovement.getPos());
    }
    Assert.executeAndAssert(movingObjects.update(bat));
  }

  public boolean hits(Bat bat) {
    if (solidBlocks.getBlocks().collidesWithNonEmpty(bat.getShape())) return true;
    boolean collided = false;
    for (PhysicalObject p : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, bat.getShape())) {
      if (p instanceof Bat) {
        continue;
      }
      collided = true;
      if (utils.isVillager(p)) {
        ((HasHealthPoints) p).getHp().damageRateLimited(bat, BITE_DMG, 300, world);
      }
    }
    return collided;
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
    BatBody body = new BatBody(object.getMovement().getX(), object.getMovement().getY(),
        (x <  object.getMovement().getX()) ? 1 : -1);
    body.addTo(renderQueue, freeFallController);
  }

  @Override
  public void damaged(Bat object, QuadTree.HasShape source, int amount) { }
}
