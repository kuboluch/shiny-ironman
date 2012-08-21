package kniemkiewicz.jqblocks.util;

/**
 * User: krzysiek
 * Date: 21.08.12
 */
public class Pair<T, E> {
  T first;
  E second;

  public static <T, E> Pair<T, E> newInstance(T t, E e) {
    return new Pair<T, E>(t, e);
  }

  private Pair(T first, E second) {
    this.first = first;
    this.second = second;
  }

  public T getFirst() {
    return first;
  }

  public E getSecond() {
    return second;
  }
}
