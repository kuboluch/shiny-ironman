package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.Player;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
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

  void add(PhysicalObject object) {
    objects.add(object);
    if (RenderableObject.class.isAssignableFrom(object.getClass())) {
      queue.add((RenderableObject)object);
    }
  }

  public PhysicalObject intersects(Shape shape) {
    // TODO: This is just lame.
    for (PhysicalObject o : objects) {
      if (o.getShape().intersects(shape)) {
        return o;
      }
    }
    return null;
  }
}
