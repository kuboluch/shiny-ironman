package kniemkiewicz.jqblocks.ingame.util.random;

import kniemkiewicz.jqblocks.util.Assert;

/**
 * User: qba
 * Date: 26.09.12
 */
public class LogNormalDistribution implements ProbabiltyDistribution {
  private float mean = 0;
  private float standardDeviation = 0.25f;
  private int expectation;
  private int minimum;

  private LogNormalDistribution(int expectation, int minimum) {
    this.expectation = expectation;
    this.minimum = minimum;
  }

  public static LogNormalDistribution withExpectationAndMinimum(int expectation, int minimum) {
    Assert.assertTrue(expectation > minimum);
    return new LogNormalDistribution(expectation, minimum);
  }

  public LogNormalDistribution mean(int mean) {
    this.mean = mean;
    return this;
  }

  public LogNormalDistribution standardDeviation(int standardDeviation) {
    this.standardDeviation = standardDeviation;
    return this;
  }

  public int next() {
    org.apache.commons.math3.distribution.LogNormalDistribution distribution =
        new org.apache.commons.math3.distribution.LogNormalDistribution(mean, standardDeviation);
    return (int)(distribution.sample() * (expectation - minimum) + minimum);
  }
}
