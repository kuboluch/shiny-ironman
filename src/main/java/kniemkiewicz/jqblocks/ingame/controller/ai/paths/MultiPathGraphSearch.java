package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * This class implements Dijkstra search in PathGraph, finding closest point from set of end points.
 * Instance of this class should be created each time when search is performed.
 * Note that this class is significantly slower than PathGraphSearch.
 *
 * User: knie
 * Date: 10/14/12
 */
final class MultiPathGraphSearch {

  public static Log logger = LogFactory.getLog(MultiPathGraphSearch.class);

  final Joint startJoint;
  final Position start;
  final SetMultimap<Edge, Float> endMap;

  // First joint in the pair is the one from which we want to move to the second one. First one is already visited and
  // is stored here only for reference, for addition to backtrackMap
  final TreeMultimap<Float, Pair<Joint, Joint>> stack = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
  Path result = null;
  boolean searchDone = false;

  // For each visited Joint, this map contains Joint from which we got there. Note that each Joint is
  // visited exactly once and this map is also used to list visited ones. Joint is always used to travel
  // to the edge to which it points to, and everywhere along this edge.
  final Map<Joint, Joint> backtrackMap = new HashMap<Joint, Joint>();
  private SortedSet<Float> keySet;
  // Those two indicate how and for what cost we got to "end". They may be updated if shortest path that gets to
  // "end.getEdge" is not the cheapest to get to "end" itself.
  private float lowestTargetCostFound = Float.MAX_VALUE;
  private Joint lowestCostLastJoint = null;
  private float selectedEndPosition = -1;

  public MultiPathGraphSearch(Position start, SetMultimap<Edge, Float> endMap) {
    this.start = start;
    // 0.5f has no meaning and won't be ever read but has to be in [0,1] range
    this.startJoint = new Joint(0.5f, start.getEdge()).with(new Joint(start.getPosition(), null));
    this.endMap = endMap;
    this.keySet = stack.keySet();
  }

  final public Path getPath() {
    if (!searchDone) {
      computePath();
      searchDone = true;
    }
    return result;
  }

  private Joint traverseGraph() {
    Float cost = 0f;
    Joint next = startJoint;
    Joint prev = null;
    while (cost < lowestTargetCostFound) {
      logger.debug("Next :" + next);
      if (!backtrackMap.containsKey(next)) {
        // Adding small cost to make algorithm choose path with smaller number of steps
        processJoint(cost + 0.1f, next);
        backtrackMap.put(next, prev);
        if (logger.isDebugEnabled()) {
          logger.debug("cost path :" + String.valueOf(cost) + " " + getPathFor(next));
        }
      }
      if (keySet.size() == 0) return lowestCostLastJoint;
      cost = keySet.first();
      Pair<Joint, Joint> p = stack.get(cost).first();
      prev = p.getFirst();
      next = p.getSecond();
      Assert.executeAndAssert(stack.remove(cost, p));
    }
    return lowestCostLastJoint;
  }

  private void processJoint(float cost, Joint next) {
    Edge edge = next.getEdge();
    float initialPos = next.getOther().getPosition();
    float edgeLength = edge.line.length();
    if (endMap.containsKey(edge)) {
      for (Float pos : Collections3.getIterable(endMap.get(edge).iterator())) {
        float dist = Math.abs(initialPos - pos) * edgeLength;
        if (lowestTargetCostFound > cost + dist) {
          lowestTargetCostFound = cost + dist;
          lowestCostLastJoint = next;
          selectedEndPosition = pos;
        }
      }
      return;
    }
    for (Joint j : edge.joints) {
      if (j == next.getOther()) {
        if (!backtrackMap.containsKey(j)) {
          backtrackMap.put(j, null); // Nobody should ever use this value.
        }
      } else {
        float dist = Math.abs(initialPos - j.getPosition()) * edgeLength;
        // We should delete all more expensive instances of "j" here to use less memory.
        stack.put(cost + dist, Pair.of(next, j));
      }
    }
  }

  List<Joint> getPathFor(Joint joint) {
    List<Joint> joints = new ArrayList<Joint>();
    while (joint != startJoint) {
      joints.add(joint);
      joint = backtrackMap.get(joint);
      assert joint != null;
    }
    Collections.reverse(joints);
    return joints;
  }

  private void computePath() {
    List<Joint> joints;
    // TODO: this is not exactly correct, if there are other targets on other edges closer than the closest point on
    // the same edge.
    if (endMap.containsKey(start.getEdge())) {
      joints = new LinkedList<Joint>();
      float bestPos = -1;
      float bestDist = Float.MAX_VALUE;
      for (Float pos : Collections3.getIterable(endMap.get(start.getEdge()).iterator())) {
        float d = Math.abs(start.getPosition() - pos);
        if (d < bestDist) {
          bestPos = pos;
          bestDist = d;
        }
      }
      selectedEndPosition = bestPos;
    } else {
      // Joint that was used to get to the target edge.
      Joint joint = traverseGraph();
      if (joint == null) return;

      // List of used joints.
      joints = getPathFor(joint);
    }
    joints.add(new Joint(selectedEndPosition, null));
    result = new Path(start,  new LinkedList<Joint>(joints));
  }
}
