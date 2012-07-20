package kniemkiewicz.jqblocks.util;

/**
 * User: krzysiek
 * Date: 19.07.12
 */

/**
 * Implementing this here to make it easy to disable this by a commandline argument later. We can also remove this
 * completely and use "assert" instead.
 */
public class Assert {
  public static void assertThat(boolean condition) {
    if (!condition) {
      throw new AssertionError(condition);
    }
  }
}
