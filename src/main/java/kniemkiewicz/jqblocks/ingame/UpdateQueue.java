package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: knie
 * Date: 7/22/12
 */
@Component
public class UpdateQueue {

  public interface UpdateController<T> {
    void update(T object, int delta);
  }

  public interface ToBeUpdated<T> {
    Class<? extends UpdateController<T>> getController();
  }

  @Autowired
  SpringBeanProvider springBeanProvider;

  Set<ToBeUpdated> objects = new HashSet<ToBeUpdated>();

  List<ToBeUpdated> toBeRemoved = new ArrayList<ToBeUpdated>();

  void update(int delta) {
    for (ToBeUpdated o : toBeRemoved) {
      objects.remove(o);
    }
    toBeRemoved.clear();
    for (ToBeUpdated o : objects) {
      UpdateController controller = (UpdateController) springBeanProvider.getBean(o.getController());
      controller.update(o, delta);
    }
  }

  public void add(ToBeUpdated ob) {
    objects.add(ob);
    toBeRemoved.remove(ob);
  }

  public void remove(ToBeUpdated ob) {
    // This often happens during iteration so we cannot just remove.
    toBeRemoved.add(ob);
  }
}
