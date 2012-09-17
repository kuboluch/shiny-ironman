package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * User: knie
 * Date: 7/22/12
 */
@Component
public final class UpdateQueue {

  public interface UpdateController<T> {
    void update(T object, int delta);
  }

  public interface ToBeUpdated<T> extends Serializable{
    Class<? extends UpdateController<? super T>> getUpdateController();
  }

  @Autowired
  SpringBeanProvider springBeanProvider;

  Set<ToBeUpdated> objects = new HashSet<ToBeUpdated>();

  Set<ToBeUpdated> toBeRemoved = new HashSet<ToBeUpdated>();

  Set<ToBeUpdated> toBeAdded = new HashSet<ToBeUpdated>();

  void updateSets() {
    for (ToBeUpdated o : toBeRemoved) {
      objects.remove(o);
    }
    toBeRemoved.clear();
    for (ToBeUpdated o : toBeAdded) {
      objects.add(o);
    }
    toBeAdded.clear();
  }

  void update(int delta) {
    updateSets();
    for (ToBeUpdated o : objects) {
      if (toBeRemoved.size() > 0) {
        // Given object may have been already deleted.
        if (toBeRemoved.contains(o)) {
          continue;
        }
      }
      Class<? extends UpdateController> clazz = o.getUpdateController();
      if (clazz != null) {
        UpdateController controller = springBeanProvider.getBean(clazz, true);
        controller.update(o, delta);
      }
    }
  }

  public void add(ToBeUpdated ob) {
    assert Assert.validateSerializable(ob);
    toBeAdded.add(ob);
    toBeRemoved.remove(ob);
  }

  public void remove(ToBeUpdated ob) {
    // This often happens during iteration so we cannot just remove.
    toBeRemoved.add(ob);
    toBeAdded.remove(ob);
  }

  public Iterator<ToBeUpdated> iterateAll() {
    updateSets();
    return objects.iterator();
  }
}
