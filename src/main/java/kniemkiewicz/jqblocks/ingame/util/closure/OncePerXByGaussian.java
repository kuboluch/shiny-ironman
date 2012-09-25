package kniemkiewicz.jqblocks.ingame.util.closure;

import java.util.Random;
import java.util.WeakHashMap;

/**
 * User: qba
 * Date: 24.09.12
 */
public class OncePerXByGaussian<T> {

  static Random random = new Random();
  GaussianDistribution gaussianDistribution;
  final Closure<T> closure;
  WeakHashMap<T, Integer> counters = new WeakHashMap<T, Integer>();

  public OncePerXByGaussian(GaussianDistribution gaussianDistribution, Closure<T> closure) {
    this.closure = closure;
    this.gaussianDistribution = gaussianDistribution;
  }

  public void maybeRunWith(T ob, int delta) {
    if (!counters.containsKey(ob)) {
      counters.put(ob, nextGaussian());
    }
    int counter = (counters.get(ob) - delta);
    if (counter <= 0) {
      closure.run(ob);
      counter = nextGaussian();
    }
    counters.put(ob, counter);
  }

  private int nextGaussian() {
    return Math.abs((int)(random.nextGaussian() * gaussianDistribution.getVariance() / 2 + gaussianDistribution.getExpectation()));
  }
}