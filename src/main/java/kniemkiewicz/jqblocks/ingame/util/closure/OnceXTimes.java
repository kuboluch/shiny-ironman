package kniemkiewicz.jqblocks.ingame.util.closure;

import java.util.Random;
import java.util.WeakHashMap;

/**
 * User: knie
 * Date: 8/31/12
 */
public class OnceXTimes<T> {

  static Random random = new Random();
  final boolean initRandomly;
  final int max;
  final Closure<T> closure;
  WeakHashMap<T, Integer> counters = new WeakHashMap<T, Integer>();

  // Use initRandomly to avoid running all instances in the same frame.
  public OnceXTimes(int max, boolean initRandomly, Closure<T> closure) {
    this.closure = closure;
    this.max = max;
    this.initRandomly = initRandomly;
  }

  public void maybeRunWith(T ob) {
    if (!counters.containsKey(ob)) {
      if (initRandomly) {
        counters.put(ob, random.nextInt(max));
      } else {
        counters.put(ob, 0);
      }
    }
    int counter = (counters.get(ob) + 1) % max;
    counters.put(ob, counter);
    if (counter == 0) {
      closure.run(ob);
    }
  }

  public void forceRun(T ob) {
    counters.put(ob, 0);
    closure.run(ob);
  }
}
