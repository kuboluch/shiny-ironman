package kniemkiewicz.jqblocks.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

  public static <T> Iterable<T> getIterable(final Iterator<T> it) {
    return new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return it;
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
}
