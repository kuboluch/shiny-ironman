package kniemkiewicz.jqblocks.util;

/**
 * User: knie
 * Date: 7/22/12
 */
public class Assert {
  public static void executeAndAssert(boolean expression) {
    assert expression;
  }

  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError(condition);
    }
  }
}
