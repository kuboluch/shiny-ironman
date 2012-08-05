package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
@Component
public class MovingObjects {

  @Autowired
  RenderQueue queue;

  @Autowired
  CollisionController collisionController;

  static final EnumSet<CollisionController.ObjectType> OBJECT_TYPE = EnumSet.of(CollisionController.ObjectType.MOVING_OBJECT);

  // TODO: It should be known which object can collide with which.
  public boolean addCollidingWithPlayer(PhysicalObject object) {
    Iterator<PhysicalObject> it = this.intersects(object.getShape());
    if (it.hasNext()) {
      if (!(it.next() instanceof Player)) return false;
    }
    Assert.executeAndAssert(collisionController.add(OBJECT_TYPE, object, true));
    return true;
  }

  public boolean add(PhysicalObject object) {
    if (this.intersects(object.getShape()).hasNext()) return false;
    Assert.executeAndAssert(collisionController.add(OBJECT_TYPE, object, true));
    return true;
  }

  public boolean addPickable(PhysicalObject object) {
    Assert.executeAndAssert(collisionController.add(OBJECT_TYPE, object, true));
    return true;
  }

  public IterableIterator<PhysicalObject> intersects(Shape shape) {
    return Collections3.getIterable(collisionController.<PhysicalObject>fullSearch(OBJECT_TYPE, shape).iterator());
  }

  public void remove(PhysicalObject po) {
    Assert.executeAndAssert(collisionController.remove(OBJECT_TYPE, po));
  }

  public Iterator<PhysicalObject> iterateAll() {
    return collisionController.<PhysicalObject>getAll(OBJECT_TYPE).iterator();
  }

  public boolean update(PhysicalObject po) {
    return collisionController.update(OBJECT_TYPE, po);
  }
}
