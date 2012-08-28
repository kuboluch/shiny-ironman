package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.ingame.FreeFallController;
import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;

import java.util.*;

/**
 * User: krzysiek
 * Date: 19.07.12
 */

/**
 * Collections are a part of JDK, Collections2 are in apache commons. So...
 */
public final class Collections3 {

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
    List<T> result = new ArrayList<T>();
    for (Object element : collection) {
      if (clazz.equals(element.getClass())) {
        result.add((T) element);
      }
    }
    return result;
  }

  public static <T> List<T> collectSubclasses(Collection<? super T> collection, Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for (Object element : collection) {
      if (clazz.isAssignableFrom(element.getClass())) {
        result.add((T) element);
      }
    }
    return result;
  }

  public static <T> List<T> collect(final Iterator<? super T> it, Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    while (it.hasNext()) {
      Object element = it.next();
      if (clazz.equals(element.getClass())) {
        result.add((T) element);
      }
    }
    return result;
  }

  public static <T> T findFirst(final Iterator<? super T> it, Class<T> clazz) {
    while (it.hasNext()) {
      Object element = it.next();
      if (clazz.equals(element.getClass())) {
        return (T) element;
      }
    }
    return null;
  }

  public static <T> List<T> collectSubclasses(final Iterator<? super T> it, Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    while (it.hasNext()) {
      Object element = it.next();
      if (clazz.isAssignableFrom(element.getClass())) {
        result.add((T) element);
      }
    }
    return result;
  }

  public static <T> IterableIterator<T> iterateOverAll(final Iterator<? extends Iterable<T>> iterables) {
    return iterateOverAllIterators(new Iterator<Iterator<T>>() {
      @Override
      public boolean hasNext() {
        return iterables.hasNext();
      }
      @Override
      public Iterator<T> next() {
        return iterables.next().iterator();
      }
      @Override
      public void remove() {  }
    });
  }

  // Add "?" until all compiles...
  public static <T> IterableIterator<T> iterateOverAllIterators(final Iterator<? extends Iterator<? extends T>> iterables) {
    if (!iterables.hasNext()) {
      return getIterable(Collections.<T>emptyList().iterator());
    }
    return new IterableIterator<T>() {
      Iterator<? extends T> currentIterator = iterables.next();

      void update() {
        while (iterables.hasNext() && !currentIterator.hasNext()) {
          currentIterator = iterables.next();
        }
      }
      @Override
      public boolean hasNext() {
        update();
        return currentIterator.hasNext();
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

  public static <T> Iterator<T> iterateOverAll(Iterator<? extends T> it1, Iterator<? extends T> it2) {
    return iterateOverAllIterators(Arrays.asList(it1, it2).iterator());
  }
}
