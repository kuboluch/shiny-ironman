package kniemkiewicz.jqblocks.ingame.ai;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: knie
 * Date: 8/31/12
 */
@Component
public class AIUtils {

  @Autowired
  CollisionController collisionController;

  @Autowired
  ControllerUtils controllerUtils;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  SolidBlocks solidBlocks;

  public PhysicalObject findNearbyVillager(PhysicalObject source, float radius) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(source.getShape());
    rect.setX(rect.getX() - radius);
    rect.setY(rect.getY() - radius);
    rect.setWidth(rect.getWidth() + 2 * radius);
    rect.setHeight(rect.getHeight() + 2 * radius);
    for (PhysicalObject po : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, rect)) {
      if (controllerUtils.isVillager(po)) {
        return po;
      }
    }
    return null;
  }

  public <T extends HasFullXYMovement & PhysicalObject> void horizontalPatrol(T object, int delta, List<PhysicalObject> collisions) {
    float prevX = object.getXYMovement().getX();
    object.getXYMovement().getYMovement().setSpeed(0);
    if (object.getXYMovement().getXMovement().getSpeed() > 0) {
      object.getXYMovement().getXMovement().acceleratePositive();
    } else {
      object.getXYMovement().getXMovement().accelerateNegative();
    }
    object.getXYMovement().update(delta);
    object.updateShape();
    if (hits(object, collisions)) {
      SingleAxisMovement xMovement = object.getXYMovement().getXMovement();
      xMovement.setPos(prevX);
      xMovement.setSpeed(-xMovement.getSpeed());
      object.updateShape();
    }
    Assert.executeAndAssert(movingObjects.update(object));
  }

  public boolean hits(HasFullXYMovement bat, List<PhysicalObject> collisions) {
    if (solidBlocks.getBlocks().collidesWithNonEmpty(bat.getShape())) return true;
    boolean collided = false;
    for (PhysicalObject p : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, bat.getShape())) {
      if (controllerUtils.isVillager(p)) {
        collisions.add(p);
        collided = true;
      }
    }
    return collided;
  }
}
