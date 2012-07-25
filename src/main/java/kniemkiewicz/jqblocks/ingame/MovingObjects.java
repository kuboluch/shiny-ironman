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

  List<PhysicalObject> objects = new ArrayList<PhysicalObject>();

  @PostConstruct
  void init() {
    this.add(player);
  }

  public void add(PhysicalObject object) {
    objects.add(object);
    if (RenderableObject.class.isAssignableFrom(object.getClass())) {
      queue.add((RenderableObject)object);
    }
  }



  public IterableIterator<PhysicalObject> intersects(Shape shape) {
    return new LinearIntersectionIterator<PhysicalObject>(objects.iterator(), shape);
  }
}
