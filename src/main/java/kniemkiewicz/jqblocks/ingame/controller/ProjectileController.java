package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.HasSource;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.Out;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 17.09.12
 */
@Component
public class ProjectileController implements UpdateQueue.UpdateController<ProjectileController.Projectile>{

  public interface Projectile<T extends Projectile> extends RenderableObject<T>,UpdateQueue.ToBeUpdated<T>,HasSource {
    // Updates movement and shape.
    void update(int delta);

    // Returning false makes it disappear from render queue.
    boolean hitWall(World world);

    void hitTarget(KillablePhysicalObject kpo, World world);
  }

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  CollisionController collisionController;

  @Autowired
  World world;

  public void add(Projectile arrow) {
    updateQueue.add(arrow);
    renderQueue.add(arrow);
  }


  private boolean checkHit(Projectile arrow, Out<PhysicalObject> physicalObject) {
    if (blocks.getBlocks().collidesWithNonEmpty(GeometryUtils.getBoundingRectangle(arrow.getShape()))) {
      return true;
    }
    for (PhysicalObject b : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, arrow.getShape())) {
      if (GeometryUtils.intersects(b.getShape(), arrow.getShape())) {
        if (b != arrow.getSource()) {
          physicalObject.set(b);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void update(Projectile object, int delta) {
    object.update(delta);
    Out<PhysicalObject> po = new Out<PhysicalObject>();
    if (checkHit(object, po)) {
      if ((po.get() != null) || !object.hitWall(world)) {
        renderQueue.remove(object);
      }
      updateQueue.remove(object);
      if ((po.get() != null) && (po.get() instanceof KillablePhysicalObject)) {
        KillablePhysicalObject kpo = (KillablePhysicalObject) po.get();
        object.hitTarget(kpo, world);
      }
    }
  }

}
