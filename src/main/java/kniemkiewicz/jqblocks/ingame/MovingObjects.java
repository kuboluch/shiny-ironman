package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.Player;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
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

  void add(PhysicalObject object) {
    objects.add(object);
    if (RenderableObject.class.isAssignableFrom(object.getClass())) {
      queue.add((RenderableObject)object);
    }
  }



  public Iterator<PhysicalObject> intersects(Shape shape) {
    return new LinearIntersectionIterator<PhysicalObject>(objects.iterator(), shape);
  }
}
