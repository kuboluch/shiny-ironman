package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
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

  final static public class Position {
    final Edge e;
    final float j;

    public Position(Edge e, float j) {
      this.e = e;
      this.j = j;
    }

    public Edge getEdge() {
      return e;
    }

    public float getPosition() {
      return j;
    }
  }

  final static public class Path {
    final Edge start;
    final List<Joint> points;

    public Path(Edge start, List<Joint> points) {
      this.start = start;
      this.points = points;
    }

    public List<Joint> getPoints() {
      return points;
    }

    public Edge getStart() {
      return start;
    }
  }

  static public final EnumSet<CollisionController.ObjectType> PATHS =
      EnumSet.of(CollisionController.ObjectType.PATHS);

  @Autowired
  CollisionController collisionController;

  @Autowired
  RenderQueue renderQueue;

  Set<PhysicalObject> sources = new HashSet<PhysicalObject>();

  Edge addEdge(Edge e) {
    collisionController.add(PATHS, e, false);
    renderQueue.add(e);
    return e;
  }

  void addSource(PhysicalObject object) {
    sources.add(object);
  }

  public Position getClosestPoint(Vector2f pos, float radius) {
    Rectangle area = new Rectangle(pos.getX() - radius, pos.getY() - radius, 2 * radius, 2 * radius);
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
      }
    }
    return new Position(closestEdge, edgePos);
  }


}
