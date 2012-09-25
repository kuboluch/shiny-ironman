package kniemkiewicz.jqblocks.ingame.util.closure;

/**
 * User: qba
 * Date: 25.09.12
 */
public class GaussianDistribution {
  final int expectation;
  final int variance;

  private GaussianDistribution(int expectation, int variance) {
    this.expectation = expectation;
    this.variance = variance;
  }

  public int getExpectation() {
    return expectation;
  }

  public int getVariance() {
    return variance;
  }

  public static GaussianDistribution withExpectationAndVariance(int expectation, int variance) {
    return new GaussianDistribution(expectation, variance);
  }
}
