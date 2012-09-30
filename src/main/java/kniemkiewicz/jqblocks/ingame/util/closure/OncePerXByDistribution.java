package kniemkiewicz.jqblocks.ingame.util.closure;

import kniemkiewicz.jqblocks.ingame.util.random.GaussianDistribution;
import kniemkiewicz.jqblocks.ingame.util.random.ProbabiltyDistribution;

import java.util.WeakHashMap;

/**
 * User: qba
 * Date: 24.09.12
 */
public class OncePerXByDistribution<T> {
  ProbabiltyDistribution probabiltyDistribution;
  final Closure<T> closure;
  WeakHashMap<T, Integer> counters = new WeakHashMap<T, Integer>();

  public OncePerXByDistribution(ProbabiltyDistribution probabiltyDistribution, Closure<T> closure) {
    this.closure = closure;
    this.probabiltyDistribution = probabiltyDistribution;
  }

  public void maybeRunWith(T ob, int delta) {
    if (!counters.containsKey(ob)) {
      counters.put(ob, probabiltyDistribution.next());
    }
    int counter = (counters.get(ob) - delta);
    if (counter <= 0) {
      closure.run(ob);
      counter = probabiltyDistribution.next();
    }
    counters.put(ob, counter);
  }

  public void reset(T ob) {
    counters.put(ob, probabiltyDistribution.next());
  }
}