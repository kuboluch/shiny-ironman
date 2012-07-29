package kniemkiewicz.jqblocks.util;

import java.util.*;

/**
 * User: krzysiek
 * Date: 19.07.12
 */

/**
 * Collections is a part of JDK, Collections2 are in apache commons. So...
 */
public class Collections3 {

  public static <T> List<T> getList(Iterator<T> it) {
    List<T> li = new ArrayList<T>();
    while (it.hasNext()) {
      li.add(it.next());
    }
    return li;
  }

  public static <T> IterableIterator<T> getIterable(final Iterator<T> it) {
    return new IterableIterator<T>() {
      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public T next() {
        return it.next();
      }

      @Override
      public void remove() {
        it.remove();
      }
    };
  }

  public static <T> List<T> collect(Collection<? super T> collection, Class<T> clazz) {
    List result = new ArrayList();
    for (Object element : collection) {
      if (clazz.equals(element.getClass())) {
        result.add(element);
      }
    }
    return result;
  }

  public static <T> List<T> collectSubclasses(Collection<? super T> collection, Class<T> clazz) {
    List result = new ArrayList();
    for (Object element : collection) {
      if (clazz.isAssignableFrom(element.getClass())) {
        result.add(element);
      }
    }
    return result;
  }

  public static <T> List<T> collect(final Iterator<? super T> it, Class<T> clazz) {
    List result = new ArrayList();
    while (it.hasNext()) {
      Object element = it.next();
      if (clazz.equals(element.getClass())) {
        result.add(element);
      }
    }
    return result;
  }

  public static <T> List<T> collectSubclasses(final Iterator<? super T> it, Class<T> clazz) {
    List result = new ArrayList();
    while (it.hasNext()) {
      Object element = it.next();
      if (clazz.isAssignableFrom(element.getClass())) {
        result.add(element);
      }
    }
    return result;
  }

  public static <T> IterableIterator<T> iterateOverAll(final List<? extends Iterable<T>> iterables) {
    // Assuming list of iterables is small. Otherwise reimplement using only iterator.
    List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
    for (Iterable<T> it : iterables) {
      iterators.add(it.iterator());
    }
    return iterateOverAllIterators(iterators);
  }

  // Add "?" as long as all compiles...
  public static <T> IterableIterator<T> iterateOverAllIterators(final List<? extends Iterator<? extends T>> iterables) {
    if (iterables.size() == 0) {
      return getIterable(Collections.<T>emptyList().iterator());
    }
    return new IterableIterator<T>() {
      int currentIterable = 0;
      Iterator<? extends T> currentIterator = iterables.get(0);

      void update() {
        while (currentIterable < iterables.size() && !currentIterator.hasNext()) {
          currentIterable++;
          if (currentIterable < iterables.size()) {
            currentIterator = iterables.get(currentIterable);
          }
        }
      }
      @Override
      public boolean hasNext() {
        update();
        return currentIterable < iterables.size();
      }

      @Override
      public T next() {
        update();
        return currentIterator.next();
      }

      @Override
      public void remove() {
        currentIterator.remove();
      }
    };
  }

  static class MultiIterator {

  }
}
