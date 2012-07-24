package kniemkiewicz.jqblocks.util;

import java.util.Iterator;

/**
 * All our iterators should extend this to allow easy foreach loop.
 * User: knie
 * Date: 7/23/12
 */
public abstract class IterableIterator<T> implements Iterable<T>,Iterator<T> {
  @Override
  public Iterator<T> iterator() {
    return this;
  }
}
