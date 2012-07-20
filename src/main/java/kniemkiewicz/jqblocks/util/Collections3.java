package kniemkiewicz.jqblocks.util;

import java.util.ArrayList;
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
}
