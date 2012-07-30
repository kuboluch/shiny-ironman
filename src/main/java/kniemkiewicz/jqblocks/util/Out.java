package kniemkiewicz.jqblocks.util;

/**
 * User: knie
 * Date: 7/28/12
 */
public final class Out<T> {
  T value = null;

  public void set(T x) {
    value = x;
  }

  public T get() {
    return value;
  }
}
