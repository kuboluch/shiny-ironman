package kniemkiewicz.jqblocks.ingame.object.bat;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/25/12
 */
@Component
public class BatController implements UpdateQueue.UpdateController<Bat>{

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World world;

  public static int BITE_DMG = 20;

  @Override
  public void update(Bat bat, int delta) {
    float prevX = bat.xMovement.getPos();
    bat.xMovement.update(delta);
    bat.rectangle.setX(bat.xMovement.getPos());
    if (hits(bat)) {
      bat.xMovement.setPos(prevX);
      bat.xMovement.setSpeed(- bat.xMovement.getSpeed());
      bat.rectangle.setX(bat.xMovement.getPos());
    }
    Assert.executeAndAssert(movingObjects.update(bat));
  }

  public boolean hits(Bat bat) {
    if (solidBlocks.getBlocks().collidesWithNonEmpty(bat.getShape())) return true;
    boolean collided = false;
    for (PhysicalObject p : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, bat.getShape())) {
      if (p != bat) {
        collided = true;
      }
      if (p instanceof Player) {
        ((HasHealthPoints) p).getHp().damageRateLimited(bat, BITE_DMG, 300, world);
      }
    }
    return collided;
  }
}
