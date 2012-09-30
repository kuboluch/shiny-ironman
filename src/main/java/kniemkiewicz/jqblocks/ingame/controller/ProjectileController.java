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

import java.util.ArrayList;
import java.util.List;

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

  List<Projectile> projectiles = new ArrayList<Projectile>();

  public void add(Projectile projectile) {
    projectiles.add(projectile);
    updateQueue.add(projectile);
    renderQueue.add(projectile);
  }


  private boolean checkHit(Projectile projectile, Out<PhysicalObject> physicalObject) {
    if (blocks.getBlocks().collidesWithNonEmpty(GeometryUtils.getBoundingRectangle(projectile.getShape()))) {
      return true;
    }
    for (PhysicalObject b : collisionController.<PhysicalObject>fullSearch(MovingObjects.MOVING, projectile.getShape())) {
      if (GeometryUtils.intersects(b.getShape(), projectile.getShape())) {
        if (b != projectile.getSource()) {
          physicalObject.set(b);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void update(Projectile projectile, int delta) {
    projectile.update(delta);
    Out<PhysicalObject> po = new Out<PhysicalObject>();
    if (checkHit(projectile, po)) {
      if ((po.get() != null) || !projectile.hitWall(world)) {
        renderQueue.remove(projectile);
      }
      updateQueue.remove(projectile);
      projectiles.remove(projectile);
      if ((po.get() != null) && (po.get() instanceof KillablePhysicalObject)) {
        KillablePhysicalObject kpo = (KillablePhysicalObject) po.get();
        projectile.hitTarget(kpo, world);
      }
    }
  }

  public List<Projectile> getProjectiles() {
    return projectiles;
  }
}
