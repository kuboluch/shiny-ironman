package kniemkiewicz.jqblocks.ingame.util.closure;

import java.util.Random;
import java.util.WeakHashMap;

/**
 * User: qba
 * Date: 23.09.12
 */
public class OncePerX<T> {

  static Random random = new Random();
  final boolean initRandomly;
  final int milliseconds;
  final Closure<T> closure;
  WeakHashMap<T, Integer> counters = new WeakHashMap<T, Integer>();

  // Use initRandomly to avoid running all instances in the same frame.
  public OncePerX(int milliseconds, boolean initRandomly, Closure<T> closure) {
    this.closure = closure;
    this.milliseconds = milliseconds;
    this.initRandomly = initRandomly;
  }

  public void maybeRunWith(T ob, int delta) {
    if (!counters.containsKey(ob)) {
      if (initRandomly) {
        counters.put(ob, random.nextInt(milliseconds));
      } else {
        counters.put(ob, 0);
      }
    }
    int counter = (counters.get(ob) + delta);
    if (counter >= milliseconds) {
      closure.run(ob);
      counter = 0;
    }
    counters.put(ob, counter);
  }
}