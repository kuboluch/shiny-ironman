package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 8/27/12
 */
public class ControllerUtils {

  public static boolean isFlying(SolidBlocks blocks, Shape shape) {
    Rectangle belowShape = new Rectangle(shape.getMinX() + 1, shape.getMaxY() + 2,
        shape.getWidth() - 4, 0);
    return !blocks.getBlocks().collidesWithNonEmpty(belowShape);
  }

  public static void pushFrom(HasFullXYMovement target, QuadTree.HasShape source, float speed) {
    float dx = target.getFullXYMovement().getX() - source.getShape().getCenterX();
    float dy = target.getFullXYMovement().getY() - source.getShape().getCenterY();
    float dd = (float)Math.sqrt(dx * dx + dy * dy);
    float speedX = dx / dd * speed;
    target.getFullXYMovement().getXMovement().setSpeed(target.getFullXYMovement().getXMovement().getSpeed() + speedX);
    float speedY = dy / dd * speed;
    target.getFullXYMovement().getYMovement().setSpeed(target.getFullXYMovement().getYMovement().getSpeed() + speedY);
  }
}
