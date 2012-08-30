package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.HitResolver;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.Collections3;
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
public final class FreeFallController {

  public interface CanFall extends PhysicalObject{
    void setYAndUpdate(float y);
  }

  List<Pair<CanFall, SingleAxisMovement>> objects = new ArrayList<Pair<CanFall, SingleAxisMovement>>();
  List<HasFullXYMovement> complexObjects = new ArrayList<HasFullXYMovement>();

  @Autowired
  SolidBlocks blocks;

  @Autowired
  CollisionController collisionController;

  public static MovementDefinition FALL_MOVEMENT = new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED);

  public void addCanFall(CanFall object) {
    objects.add(Pair.newInstance(object, FALL_MOVEMENT.getMovement(object.getShape().getY(), 0)));
  }

  public void addComplex(HasFullXYMovement hasFullXYMovement) {
    complexObjects.add(hasFullXYMovement);
  }

  public void update(int delta) {
    {
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
    {
      Iterator<HasFullXYMovement> it = complexObjects.iterator();
      while (it.hasNext()) {
        HasFullXYMovement ob = it.next();
        updateComplex(delta, it, ob);
      }
    }
  }

  public void updateComplex(int delta, Iterator<HasFullXYMovement> it, HasFullXYMovement ob) {
    ob.getFullXYMovement().getYMovement().setAcceleration(Sizes.G);
    float x = ob.getFullXYMovement().getX();
    float y = ob.getFullXYMovement().getY();
    ob.getFullXYMovement().update(delta);
    float dx = ob.getFullXYMovement().getX() - x;
    float dy = ob.getFullXYMovement().getY() - y;
    ob.updateShape();
    Rectangle bound = GeometryUtils.getBoundingRectangle(ob.getShape());
    List<Rectangle> rectangles = blocks.getBlocks().getIntersectingRectangles(bound);
    boolean deleted = false;
    for (Rectangle r : rectangles) {
      HitResolver.Decision decision = HitResolver.resolve(ob, dx, dy, r);
      ob.updateShape();
      if ((it != null) && (decision == HitResolver.Decision.TOP) && !deleted) {
        it.remove();
        deleted = true;
      }
    }
  }


  // TODO: maybe make this smarter
  Iterator<QuadTree.HasShape> getSimpleObjects() {
    List<CanFall> list = new ArrayList<CanFall>();
    for (Pair<CanFall, SingleAxisMovement> p : objects) {
      list.add(p.getFirst());
    }
    return Collections3.iterateOverAll(list.iterator(), complexObjects.iterator());
  }

  public void addObjectsInRectangle(Rectangle rect) {
    List<PhysicalObject> objects = collisionController.fullSearch(MovingObjects.OBJECT_TYPES, rect);
    for (PhysicalObject po : objects) {
      if (po instanceof CanFall) {
        addCanFall((CanFall) po);
      } else if (!(po instanceof Player) && (po instanceof HasFullXYMovement)) {
        addComplex((HasFullXYMovement)po);
      }
    }
  }
}
