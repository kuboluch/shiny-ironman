package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.Assert;
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

  static public final EnumSet<CollisionController.ObjectType> OBJECT_TYPES =
      EnumSet.of(CollisionController.ObjectType.MOVING_OBJECT, CollisionController.ObjectType.PICKABLE);

  static public final EnumSet<CollisionController.ObjectType> PICKABLE =
      EnumSet.of(CollisionController.ObjectType.PICKABLE);
  static public final EnumSet<CollisionController.ObjectType> MOVING =
      EnumSet.of(CollisionController.ObjectType.MOVING_OBJECT);

  public boolean add(PhysicalObject object, boolean checkCollisions) {
    if (object instanceof PickableObject) {
      Assert.executeAndAssert(collisionController.add(PICKABLE, object, true));
    } else {
      if (checkCollisions && collisionController.intersects(MOVING, object.getShape())) return false;
      Assert.executeAndAssert(collisionController.add(MOVING, object, true));
    }
    return true;
  }

  public void remove(PhysicalObject po) {
    if (po instanceof PickableObject) {
      Assert.executeAndAssert(collisionController.remove(PICKABLE, po));
    } else {
      Assert.executeAndAssert(collisionController.remove(MOVING, po));
    }
  }

  public Iterator<PhysicalObject> iterateAll() {
    return collisionController.<PhysicalObject>getAll(OBJECT_TYPES).iterator();
  }
}
