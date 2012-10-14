package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import com.google.common.collect.TreeMultimap;

import java.util.*;

/**
 * This class implements Dijkstra search in PathGraph.
 * Instance of this class should be created each time when search is performed.
 * TODO: Handle correctly case when there are multiple ways to reach requested edge and first found is not best.
 *
 * User: knie
 * Date: 10/14/12
 */
public final class GraphPathSearch {

  final PathGraph graph;
  final Joint startJoint;
  final PathGraph.Position start;
  final PathGraph.Position end;

  final TreeMultimap<Float, Joint> stack = TreeMultimap.create();
  PathGraph.Path result = null;

  // For each visited Joint, this map contains Joint from which we got there. Note that each Joint is
  // visited exactly once and this map is also used to list visited ones. Joint is always used to travel
  // to the edge to which it points to, and everywhere along this edge.
  final Map<Joint, Joint> backtrackMap = new HashMap<Joint, Joint>();
  private SortedSet<Float> keySet;

  public GraphPathSearch(PathGraph graph, PathGraph.Position start, PathGraph.Position end) {
    this.graph = graph;
    this.start = start;
    // 0.5f has no meaning and won't be ever read but has to be in [0,1] range
    this.startJoint = new Joint(0.5f, start.getEdge()).with(new Joint(start.getPosition(), null));
    this.end = end;
    this.keySet = stack.keySet();
  }

  final public PathGraph.Path getPath() {
    if (result == null) {
      computePath();
      assert result != null;
    }
    return result;
  }

  private Joint traverseGraph() {
    Float cost = 0f;
    Joint next = startJoint;
    Joint prev = null;
    while (next.getEdge() != end.getEdge()) {
      if (!backtrackMap.containsKey(next)) {
        processJoint(cost, next);
        backtrackMap.put(next, prev);
        prev = next;
      }

      cost = keySet.first();
      next = stack.get(cost).first();
      stack.remove(cost, next);
    }
    return next;
  }

  private void processJoint(float cost, Joint next) {
    Edge edge = next.getEdge();
    float initialPos = next.getOther().getPosition();
    float edgeLength = edge.line.length();
    for (Joint j : edge.joints) {

      float dist = Math.abs(initialPos - j.getPosition()) * edgeLength;
      // We should delete all more expensive instances of "j" here to use less memory.
      stack.put(cost + dist, j);
    }
  }

  private void computePath() {
    // Joint that was used to get to the target edge.
    Joint lastJoint = traverseGraph();
    result = new PathGraph.Path(start.getEdge(), Arrays.asList(lastJoint));
  }
}
