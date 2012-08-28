package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.peon.Peon;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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
    float dx = target.getFullXYMovement().getX() - source.getShape().getCenterX();
    float dy = target.getFullXYMovement().getY() - source.getShape().getCenterY();
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float speedX = dx / dd * speed;
    target.getFullXYMovement().getXMovement().setSpeed(target.getFullXYMovement().getXMovement().getSpeed() + speedX);
    float speedY = dy / dd * speed;
    target.getFullXYMovement().getYMovement().setSpeed(target.getFullXYMovement().getYMovement().getSpeed() + speedY);
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
}
