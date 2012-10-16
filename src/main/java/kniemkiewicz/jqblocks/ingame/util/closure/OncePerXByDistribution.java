package kniemkiewicz.jqblocks.ingame.util.closure;

import kniemkiewicz.jqblocks.ingame.util.random.ProbabilityDistribution;

import java.util.WeakHashMap;

/**
 * User: qba
 * Date: 24.09.12
 */
public class OncePerXByDistribution<T> {
  ProbabilityDistribution probabilityDistribution;
  final Closure<T> closure;
  WeakHashMap<T, Integer> counters = new WeakHashMap<T, Integer>();

  public OncePerXByDistribution(ProbabilityDistribution probabilityDistribution, Closure<T> closure) {
    this.closure = closure;
    this.probabilityDistribution = probabilityDistribution;
  }

  public void maybeRunWith(T ob, int delta) {
    if (!counters.containsKey(ob)) {
      counters.put(ob, probabilityDistribution.next());
    }
    int counter = (counters.get(ob) - delta);
    if (counter <= 0) {
      closure.run(ob);
      counter = probabilityDistribution.next();
    }
    counters.put(ob, counter);
  }

  public void reset(T ob) {
    counters.put(ob, probabilityDistribution.next());
  }
}