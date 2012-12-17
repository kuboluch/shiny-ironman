package kniemkiewicz.jqblocks.ingame.util.random;

import java.util.Random;

/**
 * User: qba
 * Date: 25.09.12
 */
public class GaussianDistribution implements ProbabilityDistribution {
  static Random random = new Random();
  private int expectation;
  private int standardDeviation = 2; // 95% within (expectation - range) and (expectation + range);
  private int range;

  private GaussianDistribution(int expectation, int range) {
    this.expectation = expectation;
    this.range = range;
  }

  public static GaussianDistribution withExpectationAndRange(int expectation, int range) {
    return new GaussianDistribution(expectation, range);
  }

  public GaussianDistribution standardDeviation(int standardDeviation) {
    this.standardDeviation = standardDeviation;
    return this;
  }

  public int next() {
    return Math.abs((int)(getGaussian(random, range / standardDeviation, expectation)));
  }


  public static double getGaussian(Random random, float min, float range, float average) {
    return Math.max(min, getGaussian(random, range, average));
  }

  public static double getGaussian(Random random, float range, float average) {
    return random.nextGaussian() * range + average;
  }
}
