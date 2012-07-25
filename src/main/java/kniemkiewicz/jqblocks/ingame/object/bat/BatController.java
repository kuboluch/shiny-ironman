package kniemkiewicz.jqblocks.ingame.object.bat;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
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
  }

  public boolean hits(Bat bat) {
    if (solidBlocks.intersects(bat.getShape()).hasNext()) return true;
    for (PhysicalObject p : movingObjects.intersects(bat.getShape())) {
      if (p != bat) return true;
    }
    return false;
  }
}
