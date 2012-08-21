package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 21.08.12
 */
@Component
public class FreeFallController {

  public interface CanFall extends PhysicalObject{
    void setYAndUpdate(float y);
  }

  List<Pair<CanFall, SingleAxisMovement>> objects = new ArrayList<Pair<CanFall, SingleAxisMovement>>();

  @Autowired
  SolidBlocks blocks;

  public void add(CanFall object) {
    objects.add(Pair.newInstance(object, new SingleAxisMovement(Sizes.MAX_FALL_SPEED, 0, object.getShape().getY(), 0)));
  }

  public void update(int delta) {
    Iterator<Pair<CanFall, SingleAxisMovement>> it = objects.iterator();
    while (it.hasNext()) {
      Pair<CanFall, SingleAxisMovement> p = it.next();
      p.getSecond().setAcceleration(Sizes.G);
      p.getSecond().update(delta);
      p.getFirst().setYAndUpdate(p.getSecond().getPos());
      Rectangle bound = GeometryUtils.getBoundingRectangle(p.getFirst().getShape());
      List<Rectangle> rectangles = blocks.getBlocks().getIntersectingRectangles(bound);
      if (rectangles.size() > 0) {
        float y = HitResolver.resolveSimpleTop(rectangles, p.getFirst().getShape());
        p.getFirst().setYAndUpdate(y);
        it.remove();
      }
    }
  }

  // TODO: maybe make this smarter
  Iterator<CanFall> getObjects() {
    List<CanFall> list = new ArrayList<CanFall>();
    for (Pair<CanFall, SingleAxisMovement> p : objects) {
      list.add(p.getFirst());
    }
    return list.iterator();
  }
}
