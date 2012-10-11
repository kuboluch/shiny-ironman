package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import com.google.common.base.Function;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderBackground;
import kniemkiewicz.jqblocks.ingame.content.transport.ladder.LadderDefinition;
import kniemkiewicz.jqblocks.ingame.controller.CollisionController;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 01.10.12
 */
@Component
public class GraphGenerator {

  @Autowired
  PathGraph graph;

  @Autowired
  CollisionController collisionController;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Backgrounds backgrounds;

  public void addSource(PhysicalObject object) {
    Shape shape = object.getShape();
    if (collisionController.intersects(PathGraph.PATHS, shape)) return;
    graph.addSource(object);

    Edge e = addFlatEdgeFor(shape.getMinX() + shape.getWidth() / 2, shape.getMinY() + shape.getHeight() - 1);
    assert e != null;
    assert GeometryUtils.intersects(e.getShape(), shape);
  }

  // x and y are indexes in RawEnumTable, not actual position
  private int getLastFlatX(int x, int y, int direction) {
    assert Math.abs(direction) == 1;
    int res = x;
    RawEnumTable<WallBlockType> table = solidBlocks.getBlocks();
    while (table.get(res + direction, y) == WallBlockType.EMPTY && table.get(res + direction, y + 1) != WallBlockType.EMPTY) {
      res+=direction;
    }
    return res;
  }

  // Returns false iff there is already an edge of the same type within given shape.
  // Shape can be just the shape of edge but it is better to have slightly bigger one to make sure that given
  // edge would collide with itself.
  boolean joinOrFail(Edge newEdge, Shape shape) {
    List<Edge> edges = collisionController.fullSearch(PathGraph.PATHS, shape);
    for (Edge e : edges) {
      if (e.type == newEdge.type) return false;
    }
    for (Edge e : edges) {
      joinEdge(newEdge, e);
    }
    graph.addEdge(newEdge);
    return true;
  }

  private static final int JOIN_DISTANCE_SQUARED = 9;

  //TODO: add tests!!!
  void joinEdge(Edge newEdge, Edge edge) {
    Vector2f intersection = newEdge.line.intersect(edge.line, true);
    if (intersection == null) {
      // Lines do not intersect but they are very close(probably almost parallel). Let's try add joints at the ends,
      // assuming they can be used, depending on which is faster.
      for (Edge e : new Edge[]{newEdge, edge}) {
        Edge other = (e == newEdge ? edge : newEdge);
        {
          Vector2f p1 = new Vector2f(other.line.getX1(), other.line.getY1());
          if (e.line.distanceSquared(p1) < JOIN_DISTANCE_SQUARED) {
            // BUG: p1 doesn't have to be on
            e.addJoint(p1, other).with(other.addJoint(p1, e));
          }
        }
        {
          Vector2f p2 = new Vector2f(other.line.getX2(), other.line.getY2());
          if (e.line.distanceSquared(p2) < JOIN_DISTANCE_SQUARED) {
            e.addJoint(p2, other).with(other.addJoint(p2, e));
          }
        }
      }
    } else {
      newEdge.addJoint(intersection, edge).with(edge.addJoint(intersection, newEdge));
    }
  }

  private Edge addFlatEdgeFor(float x, float y) {
    RawEnumTable<WallBlockType> table = solidBlocks.getBlocks();
    int i = table.toXIndex((int)x);
    int j = table.toYIndex((int)y);
    assert table.get(i, j) == WallBlockType.EMPTY;
    assert table.get(i, j + 1) != WallBlockType.EMPTY;
    int i1 = getLastFlatX(i, j, -1);
    int i2 = getLastFlatX(i, j, 1);
    float x1 = Sizes.MIN_X + i1 * Sizes.BLOCK;
    float x2 = Sizes.MIN_X + (i2 + 1) * Sizes.BLOCK;
    Rectangle r = new Rectangle(x1, y - 1, x2, y + 2);
    Edge e = new Edge(new Line(x1, y, x2, y), Edge.Type.FLAT);
    if (joinOrFail(e, r)) {
      spreadEdges(e);
      return e;
    } else {
      return null;
    }
  }

  static final float MAX_LADDER_DISTANCE = Sizes.BLOCK;

  private int findLastLadderPart(List<LadderBackground> ladders, int current, int direction, boolean vertical) {
    while ((current + direction > -1) && (current + direction < ladders.size())) {
      int next = current + direction;
      if (vertical) {
        if (Math.abs(ladders.get(next).getShape().getMinY() - ladders.get(current).getShape().getMinY()) > LadderDefinition.HEIGHT + MAX_LADDER_DISTANCE) {
          return current;
        }
      } else {
        if (Math.abs(ladders.get(next).getShape().getMinX() - ladders.get(current).getShape().getMinX()) > LadderDefinition.WIDTH + MAX_LADDER_DISTANCE) {
          return current;
        }
      }
      current = next;
    }
    return current;
  }

  private Rectangle getSurrounding(Line l) {
    Rectangle r = GeometryUtils.getBoundingRectangle(l);
    return GeometryUtils.getRectangleCenteredOn(l, r.getWidth() + 5, r.getHeight() + 5);
  }

  private void addLadderVerticalEdgeFor(LadderBackground be) {
    Shape shape = be.getShape();
    Rectangle r = new Rectangle(shape.getMinX(), Sizes.MIN_Y, shape.getWidth(), Sizes.LEVEL_SIZE_Y);
    List<LadderBackground> allLadders = Collections3.collectSubclasses(backgrounds.intersects(r), LadderBackground.class);
    Collections3.sortByFunction(allLadders, new Function<LadderBackground, Float>() {
      @Override
      public Float apply(LadderBackground input) {
        return input.getShape().getMinY();
      }
    });
    int i = allLadders.indexOf(be);
    assert i >= 0;
    List<LadderBackground> ladders = allLadders.subList(findLastLadderPart(allLadders, i, -1, true),findLastLadderPart(allLadders, i, 1, true) + 1);

    Edge e = new Edge(new Line(shape.getMinX(), ladders.get(0).getShape().getMinY(), shape.getMinX(), ladders.get(ladders.size() - 1).getShape().getMinY() + LadderDefinition.HEIGHT - 1), Edge.Type.VERTICAL_LADDER);
    if (joinOrFail(e, getSurrounding(e.line))) {
      Edge firstHorizontal = null;
      for (LadderBackground ladder : ladders) {
        Edge horizontal = addLadderHorizontalEdgeFor(ladder);
        if (firstHorizontal == null) {
          firstHorizontal = horizontal;
        }
      }
      // Trying to add a line on top of set of ladders.
      if (firstHorizontal != null) {
        Line f = firstHorizontal.line;
        Line l = new Line(f.getX1(), f.getY() - LadderDefinition.HEIGHT, f.getX2(), f.getY() - LadderDefinition.HEIGHT);
        Edge onTop = new Edge(l, Edge.Type.HORIZONTAL_LADDER);
        if (joinOrFail(onTop, getSurrounding(onTop.line))) {
          spreadEdges(onTop);
        }
      }
      spreadEdges(e);
    }
  }

  private Edge addLadderHorizontalEdgeFor(LadderBackground be) {
    Shape shape = be.getShape();
    Rectangle r = new Rectangle(Sizes.MIN_X, shape.getMinY(), Sizes.LEVEL_SIZE_X, shape.getHeight());
    List<LadderBackground> allLadders = Collections3.collectSubclasses(backgrounds.intersects(r), LadderBackground.class);
    Collections3.sortByFunction(allLadders, new Function<LadderBackground, Float>() {
      @Override
      public Float apply(LadderBackground input) {
        return input.getShape().getMinX();
      }
    });
    int i = allLadders.indexOf(be);
    assert i >= 0;
    List<LadderBackground> ladders = allLadders.subList(findLastLadderPart(allLadders, i, -1, false),findLastLadderPart(allLadders, i, 1, false) + 1);

    Edge e = new Edge(new Line(ladders.get(0).getShape().getMinX(), shape.getMinY() + LadderDefinition.HEIGHT - 1, ladders.get(ladders.size() - 1).getShape().getMinX() + LadderDefinition.WIDTH, shape.getMinY() + LadderDefinition.HEIGHT -1), Edge.Type.HORIZONTAL_LADDER);
    if (joinOrFail(e, GeometryUtils.getBoundingRectangle(e.line))) {
      for (LadderBackground ladder : ladders) {
        addLadderVerticalEdgeFor(ladder);
      }
      spreadEdges(e);
      return e;
    } else {
      return null;
    }

  }


  private void spreadEdges(Edge e) {
    if ((e.type != Edge.Type.VERTICAL_LADDER) && (e.type != Edge.Type.HORIZONTAL_LADDER)) {
      Iterator<BackgroundElement> it = backgrounds.intersects(e.getShape());
      while (it.hasNext()) {
        BackgroundElement be = it.next();
        if (be instanceof LadderBackground) {
          addLadderVerticalEdgeFor((LadderBackground) be);
        }
      }

    }
  }
}
