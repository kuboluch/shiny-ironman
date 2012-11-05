package kniemkiewicz.jqblocks.ingame.content.creature.peon;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.*;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.closure.OnceXTimes;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * User: krzysiek
 * Date: 30.10.12
 */
@Component
public class PeonMoveController {

  @Autowired
  PathGraph pathGraph;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  CollisionController collisionController;

  private OnceXTimes<Peon> findPathClosure = new OnceXTimes<Peon>(90, true, new Closure<Peon>() {
    @Override
    public void run(Peon peon) {
      QuadTree.HasShape target = peon.getTarget();
      Position destination = pathGraph.getClosestPoint(GeometryUtils.getRectangleCenteredOn(target.getShape().getCenterX(), GeometryUtils.getMaxY(target.getShape()), Sizes.BLOCK * 3));
      if (destination == null && peon.getCurrentPath() != null && peon.getCurrentPath().fastGetPath() != null) {
        return;
      }
      if (destination == null) {
        Rectangle r = GeometryUtils.getRectangleCenteredOn(peon.getShape().getCenterX(), GeometryUtils.getMaxY(peon.getShape()), 24);
        Position origin = pathGraph.getClosestPoint(r);
        if (origin == null) return;
        Path p = new Path(origin, new LinkedList<Joint>());
        peon.setCurrentPath(new PermPath(p, p.getStart(), pathGraph, collisionController));
        return;
      }
      if (peon.getCurrentPath() == null) {
        // new Peon or sth
        Rectangle r = GeometryUtils.getRectangleCenteredOn(peon.getShape().getCenterX(), GeometryUtils.getMaxY(peon.getShape()), 24);
        Position origin = pathGraph.getClosestPoint(r);
        if (origin == null) {
          peon.setCurrentPath(null);
          return;
        }
        if (destination == null) {
          destination = origin;
        }
        peon.setCurrentPath(pathGraph.getPermPath(origin, destination));
      } else {
        peon.getCurrentPath().switchDestinationTo(destination);
      }
    }
  });

  void updateOutsideGraph(Peon peon, int delta) {
    // TODO(krzysiek): Maybe this should be handled by PeonController. Anyway, add something more fancy.
    freeFallController.updateComplex(delta, null, peon);
    peon.setCurrentPath(null);
  }

  public void update(Peon peon, int delta) {
    if (peon.getTarget() == null) {
      if (!collisionController.intersects(PathGraph.PATHS, peon.getShape())) {
        updateOutsideGraph(peon, delta);
      }
      return;
    }
    if (peon.getCurrentPath() != null) {
      findPathClosure.maybeRunWith(peon);
    } else {
      // We really need a path.
      findPathClosure.forceRun(peon);
    }
    boolean outsideGraph = true;
    Path path = null;
    if (peon.getCurrentPath() != null) {
      // This also updated "outsideGraph()"
      path = peon.getCurrentPath().safeGetPath();
      outsideGraph = peon.getCurrentPath().outsideGraph();
    }
    if (outsideGraph) {
      updateOutsideGraph(peon, delta);
      return;
    }
    float speed = Peon.MAX_PEON_SPEED * delta;
    if (path != null) {
      peon.getXYMovement().getYMovement().setSpeed(0);
      path.progressWithSpeed(speed);
      peon.update();
    }
  }
}
