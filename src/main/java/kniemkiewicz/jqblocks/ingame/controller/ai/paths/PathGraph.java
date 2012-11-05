package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.hud.info.TimingInfo;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
  Configuration configuration;

  @Autowired
  CollisionController collisionController;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  TimingInfo timingInfo;

  boolean RENDER_EDGES;

  @PostConstruct
  void init() {
    RENDER_EDGES = configuration.getBoolean("PathGraph.RENDER_EDGES", true);
  }

  Edge addEdge(Edge e) {
    collisionController.add(PATHS, e, false);
    if (RENDER_EDGES) {
      renderQueue.add(e);
    }
    return e;
  }

  void clear() {
    for (Edge e : collisionController.<Edge>getAll(PATHS)) {
      if (RENDER_EDGES) {
        renderQueue.remove(e);
      }
      e.type = Edge.Type.INVALID;
    }
    collisionController.clear(PATHS);
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
      float ePos = e.getClosestPos(pos, 2.5f * radius2);
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

  public Path getPath(Position start, Position end) {
    assert start != null;
    assert end != null;
    TimingInfo.Timer timer = timingInfo.getTimer("getPath");
    Path p = new PathGraphSearch(start, end).getPath();
    timer.record();
    return p;
  }

  public PermPath getPermPath(Position start, Position end) {
    return new PermPath(getPath(start, end), end, this, collisionController);
  }

  public <T extends QuadTree.HasShape> Pair<T, Path> getPathToClosest(Position start, List<? extends T> candidates) {
     List<Position> positions = Collections3.collect(candidates, new Function<T, Position>() {
       @Override
       public Position apply(T input) {
         return getClosestPoint(GeometryUtils.getBoundingRectangle(input.getShape()));
       }
     });
    HashMultimap<Edge, Float> endMap = HashMultimap.create(candidates.size(), 2);
    for (Position p : positions) {
      endMap.put(p.getEdge(), p.getPosition());
    }
    Path path = new MultiPathGraphSearch(start, endMap).getPath();
    if (path == null) return null;
    int index = positions.indexOf(path.getEnd());
    assert index >= 0;
    if (index < 0) return null;
    return Pair.of(candidates.get(index), path);
  }
}
