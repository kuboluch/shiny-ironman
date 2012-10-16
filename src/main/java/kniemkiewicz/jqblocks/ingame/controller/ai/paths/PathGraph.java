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
    Edge e;
    float j;

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

    public void setEdge(Edge e) {
      this.e = e;
    }

    public void setPosition(float j) {
      this.j = j;
    }

    public Vector2f getPoint() {
      return e.getPointFor(j);
    }
  }

  final static public class Path {
    final Position start;
    final LinkedList<Joint> points;

    public Path(Position start, LinkedList<Joint> points) {
      this.start = start;
      this.points = points;
    }

    public List<Joint> getPoints() {
      return points;
    }

    public Position getStart() {
      return start;
    }

    // dist here is a fraction of current edge.
    // This method may progress over to next edge, but it won't go further.
    // In such case remaining "dist" is returned, otherwise method returns 0.
    public float progress(float dist) {
      Joint next = points.getFirst();
      float currentDistance = next.getPosition() - start.getPosition(); // may be negative.
      if (Math.abs(currentDistance) < dist) {
        if (points.size() > 1) {
          start.setEdge(next.getEdge());
          start.setPosition(next.getOther().getPosition());
        }
        points.removeFirst();
        return Math.abs(currentDistance) - dist;
      } else {
        if (currentDistance > 0) {
          start.setPosition(start.getPosition() + dist);
        } else {
          start.setPosition(start.getPosition() - dist);
        }
        return 0;
      }
    }

    public float progressWithSpeed(float speed) {
      return progress(speed / start.getEdge().getShape().length());
    }

    @Override
    public String toString() {
      return "Path{" + start + "," + points + "}";
    }

    List<Position> computePositions() {
      List<Position> result = new ArrayList<Position>();
      result.add(start);
      Edge prev = start.getEdge();
      for (Joint j : points) {
        result.add(new Position(prev, j.getPosition()));
        prev = j.getEdge();
      }
      return result;
    }

    public List<Line> getLines() {
      List<Line> lines = new ArrayList<Line>();
      Edge edge = start.getEdge();
      float pos = start.getPosition();
      for (Joint j : points) {
        lines.add(GeometryUtils.getLineInterval(edge.line, pos, j.getPosition()));
        if (j.getOther() == null) {
          // This should happen only on last iteration.
          edge = null;
        } else {
          edge = j.getEdge();
          pos = j.getOther().getPosition();
        }
      }
      return lines;
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
        minDistance = dis;
      }
    }
    return new Position(closestEdge, edgePos);
  }


}
