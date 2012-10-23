package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: krzysiek
 * Date: 26.09.12
 */
@Component
final public class PathGraph {

  static public final EnumSet<CollisionController.ObjectType> PATHS =
      EnumSet.of(CollisionController.ObjectType.PATHS);

  @Autowired
  CollisionController collisionController;

  @Autowired
  RenderQueue renderQueue;

  Edge addEdge(Edge e) {
    collisionController.add(PATHS, e, false);
    renderQueue.add(e);
    return e;
  }

  void clear() {
    for (Edge e : collisionController.<Edge>getAll(PATHS)) {
      renderQueue.remove(e);
      e.type = Edge.Type.INVALID;
    }
    collisionController.clear(PATHS);
  }

  public Position getClosestPoint(Vector2f pos, float radius) {
    Rectangle area = new Rectangle(pos.getX() - radius, pos.getY() - radius, 2 * radius, 2 * radius);
    return getClosestPoint(pos, radius, area);
  }

  public Position getClosestPoint(Rectangle area) {
    float radius = area.getBoundingCircleRadius();
    Vector2f pos = new Vector2f(area.getCenter());
    return getClosestPoint(pos, radius, area);
  }

  private Position getClosestPoint(Vector2f pos, float radius, Rectangle area) {
    float minDistance = Float.MAX_VALUE;
    Edge closestEdge = null;
    float edgePos = 0;
    float radius2 = radius * radius;
    for (Edge e : collisionController.<Edge>fullSearch(PATHS, area)) {
      float ePos = e.getClosestPos(pos, 2 * radius2);
      float dis = e.getPointFor(ePos).distanceSquared(pos);
      if (dis < minDistance) {
        closestEdge = e;
        edgePos = ePos;
        minDistance = dis;
      }
    }
    if (closestEdge == null) {
      return null;
    } else {
      return new Position(closestEdge, edgePos);
    }
  }


}
