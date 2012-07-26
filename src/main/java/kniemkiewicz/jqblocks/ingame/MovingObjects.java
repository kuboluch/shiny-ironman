package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
@Component
public class MovingObjects {
  @Autowired
  Player player;

  @Autowired
  RenderQueue queue;

  HashSet<PhysicalObject> objects = new HashSet<PhysicalObject>();

  @PostConstruct
  void init() {
    queue.add(player);
    objects.add(player);
  }

  public boolean add(PhysicalObject object) {
    if (this.intersects(object.getShape()).hasNext()) return false;
    objects.add(object);
    return true;
  }



  public IterableIterator<PhysicalObject> intersects(Shape shape) {
    return new LinearIntersectionIterator<PhysicalObject>(objects.iterator(), shape);
  }

  public void remove(PhysicalObject po) {
    objects.remove(po);
  }
}
