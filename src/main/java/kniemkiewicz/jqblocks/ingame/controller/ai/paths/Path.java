package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Line;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* User: krzysiek
* Date: 23.10.12
*/
final public class Path {
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
    if (points.size() == 0) return dist;
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
