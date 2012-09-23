package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.Peon;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ControllerUtils {

  @Autowired
  SolidBlocks blocks;

  @Autowired
  World world;

  @Autowired
  PlayerController playerController;

  @Autowired
  EventBus eventBus;

  private Set<Class<? extends HasHealthPoints>> villagerClasses;

  public ControllerUtils() {
    villagerClasses = new HashSet<Class<? extends HasHealthPoints>>();
    villagerClasses.add(Player.class);
    villagerClasses.add(Peon.class);
  }

  public boolean isFlying(Shape shape) {
    Rectangle belowShape = new Rectangle(shape.getMinX() + 1, shape.getMaxY() + 2,
        shape.getWidth() - 4, 0);
    return !blocks.getBlocks().collidesWithNonEmpty(belowShape);
  }

  public static float DEFAULT_PUSH_BACK = Player.MAX_X_SPEED;

  public void pushFrom(HasFullXYMovement target, QuadTree.HasShape source, float speed) {
    float dx = target.getXYMovement().getX() - source.getShape().getCenterX();
    float dy = target.getXYMovement().getY() - source.getShape().getCenterY();
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float speedX = dx / dd * speed;
    if (Math.abs(target.getXYMovement().getXMovement().getSpeed() + speedX) > Math.abs(speedX)) {
      target.getXYMovement().getXMovement().setSpeed(speedX);
    } else {
      target.getXYMovement().getXMovement().setSpeed(target.getXYMovement().getXMovement().getSpeed() + speedX);
    }
    float speedY = dy / dd * speed;
    if (Math.abs(target.getXYMovement().getYMovement().getSpeed() + speedY) > Math.abs(speedY)) {
      target.getXYMovement().getYMovement().setSpeed(speedY);
    } else {
      target.getXYMovement().getYMovement().setSpeed(target.getXYMovement().getYMovement().getSpeed() + speedY);
    }
  }

  public boolean isVillager(PhysicalObject po) {
    assert villagerClasses.size() > 0;
    return villagerClasses.contains(po.getClass());
  }

  public void damageTouchedVillagers(QuadTree.HasShape source, int damage) {
    Shape shape = source.getShape();
    for (PhysicalObject p : world.getCollisionController().<PhysicalObject>fullSearch(MovingObjects.MOVING, shape)) {
      if (p instanceof Player) {
        ((HasHealthPoints) p).getHp().damageRateLimited(source, damage, 300, world);
      }
    }
  }

  // Returns (x2, y2) - (x1, y1) scaled to given length.
  public Vector2f getVectorScaled(float x1, float y1, float x2, float y2, float length) {
    Vector2f v = new Vector2f(x2 - x1, y2 - y1);
    return v.scale(length / v.length());
  }

  public boolean resolveCollisionsWithWalls(HasFullXYMovement ob) {
    Rectangle bound = GeometryUtils.getBoundingRectangle(ob.getShape());
    List<Rectangle> rectangles = blocks.getBlocks().getIntersectingRectangles(bound);
    boolean collided = false;
    float dx = ob.getXYMovement().getXMovement().getSpeed();
    float dy = ob.getXYMovement().getYMovement().getSpeed();
    for (Rectangle r : rectangles) {
      HitResolver.resolve(ob, dx, dy, r);
      ob.updateShape();
      collided = true;
    }
    return collided;
  }



  // Returns pair dx,dy of vector with given radius, pointing towards mouse from 'from'
  public Vector2f getCurrentDirection(float radius, Vector2f from) {
    boolean leftFaced = playerController.getPlayer().isLeftFaced();
    float x0 = from.getX();
    float y0 = from.getY();
    float mouseX = eventBus.getLatestMouseMovedEvent().getNewScreenX();
    float mouseY = eventBus.getLatestMouseMovedEvent().getNewScreenY();
    float dist = (float) Math.sqrt((mouseX - x0) * (mouseX - x0) + (mouseY - y0) * (mouseY - y0));
    float dx;
    float dy;
    if (dist == 0) {
      dx = leftFaced ? - radius : radius;
      dy = 0;
    } else {
      dx = radius * (mouseX - x0) / dist;
      dy = radius * (mouseY - y0) / dist;
    }
    if (((dx > 0) && leftFaced) || (dx < 0 && !leftFaced)) {
      if (dy > 0) {
        dx = 0;
        dy = radius;
      } else {
        dx = 0;
        dy = - radius;
      }
    }
    return new Vector2f(dx, dy);
  }
}
