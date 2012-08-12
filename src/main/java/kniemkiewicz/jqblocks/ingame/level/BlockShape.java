package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.util.Assert;

import java.util.*;

/**
 * User: knie
 * Date: 8/3/12
 */
public class BlockShape {

  static class Interval implements Comparable<Interval>{

    Interval(int begin, int end) {
      this.begin = begin;
      this.end = end;
    }

    public int begin;
    public int end;


    @Override
    public int compareTo(Interval o) {
      if (begin < o.begin) return -1;
      if (begin > o.begin) return 1;
      return 0;
    }

    @Override
    public String toString() {
      return "Interval{" + begin + "," + end + "}";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Interval interval = (Interval) o;

      if (begin != interval.begin) return false;
      if (end != interval.end) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = begin;
      result = 31 * result + end;
      return result;
    }
  }

  Map<Integer, TreeSet<Interval>> intervals = new TreeMap<Integer, TreeSet<Interval>>();
  Map<Integer, TreeMap<Integer, Interval>> points = new HashMap<Integer, TreeMap<Integer, Interval>>();

  Interval getIntervalEndingWith(Integer x, Integer y) {
    if (points.containsKey(y)) {
      if (points.get(y).containsKey(x)) {
        return points.get(y).get(x);
      }
    }
    return null;
  }

  Set<Interval> getOverlapping(Integer y, Interval interval) {
    if (!points.containsKey(y)) {
      points.put(y, new TreeMap<Integer, Interval>());
    }
    TreeMap<Integer, Interval> p = points.get(y);
    HashSet<Interval> overlapping = new HashSet<Interval>();
    for (Integer i : p.navigableKeySet().subSet(interval.begin, interval.end + 1)) {
      overlapping.add(p.get(i));
    }
    return overlapping;
  }

  void addInternal(Integer y, Interval interval) {
    if (!intervals.containsKey(y)) {
      intervals.put(y, new TreeSet<Interval>());
    }
    assert !intervals.get(y).contains(interval);
    intervals.get(y).add(interval);
    if (!points.containsKey(y)) {
      points.put(y, new TreeMap<Integer, Interval>());
    }
    TreeMap<Integer, Interval> p = points.get(y);
    assert !p.containsKey(interval.begin);
    assert !p.containsKey(interval.end);
    p.put(interval.begin, interval);
    p.put(interval.end, interval);
  }

  boolean remove(Integer y, Interval interval) {
    if (!intervals.get(y).remove(interval)) return false;
    assert points.get(y).containsKey(interval.begin);
    Assert.executeAndAssert(points.get(y).remove(interval.begin) != null);
    Assert.executeAndAssert(points.get(y).remove(interval.end) != null);
    return true;
  }

  /**
   * Interval will be updated to match it's size after joining with overlapping ones.
   */
  public void add(Integer y, Interval interval) {
    for (Interval i : getOverlapping(y, interval)) {
      if (i.begin < interval.begin) {
        interval.begin = i.begin;
      }
      if (i.end > interval.end) {
        interval.end = i.end;
      }
      remove(y, i);
    }
    addInternal(y, interval);
  }

  public void addCircle(int x, int y, float radius) {
    int r = (int)radius;
    for (int i = -r; i < r; i++) {
      int dx = (int)Math.sqrt(r*r - i*i);
      Interval interval = new Interval(x - dx, x + dx + 1);
      add(y + i, interval);
    }
  }

  public Map<Integer, TreeSet<Interval>> getIntervals() {
    return intervals;
  }
}
