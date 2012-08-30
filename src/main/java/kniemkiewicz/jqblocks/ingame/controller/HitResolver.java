package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.List;

/**
 * User: krzysiek
 * Date: 17.07.12
 */
public class HitResolver {

  public static float resolveSimpleTop(List<Rectangle> rectangles, Shape shape) {
    Rectangle rectangle = GeometryUtils.getBoundingRectangle(shape);
    float y = GeometryUtils.getMaxY(rectangle);
    for (Rectangle r : rectangles) {
      if (GeometryUtils.intersects(r, shape)) {
        if (y > r.getY()) {
          y = r.getY();
        }
      }
    }
    return y - shape.getHeight();
  }

  public enum Decision {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    IGNORE
  }

  /**
   * TODO: This should be implemented as a common operation on all moving panelItems
   * Player.getShape has to be partially inside rect. dx and dy give the direction of recent player movement.
   */
  public static Decision resolve(HasFullXYMovement ob, float dx, float dy, Rectangle rect) {
    Rectangle shape = GeometryUtils.getBoundingRectangle(ob.getShape());
    Decision decision = decide(shape, dx, dy, rect);
    SingleAxisMovement xMovement = ob.getFullXYMovement().getXMovement();
    SingleAxisMovement yMovement = ob.getFullXYMovement().getYMovement();
    switch (decision) {
      case TOP:
        yMovement.setSpeed(0);
        yMovement.setPos(rect.getY() - shape.getHeight());
        break;
      case BOTTOM:
        yMovement.setSpeed(0);
        yMovement.setPos(GeometryUtils.getMaxY(rect) + 1);
        break;
      case LEFT:
        xMovement.setSpeed(0);
        xMovement.setPos(rect.getX() - shape.getWidth());
        break;
      case RIGHT:
        xMovement.setSpeed(0);
        xMovement.setPos(GeometryUtils.getMaxX(rect) + 1);
        break;
      case IGNORE:
        break;
    }
    assert xMovement.getPos() > Sizes.MIN_X - rect.getWidth() - 1;
    assert xMovement.getPos() < Sizes.MAX_X + 100;
    assert yMovement.getPos() > Sizes.MIN_Y - rect.getHeight() - 1;
    assert yMovement.getPos() < Sizes.MAX_Y + 100;
    return decision;
  }

  static Decision decide(Rectangle player, float dx, float dy, Rectangle rect) {
    if ((dx == 0) && (dy == 0)) return Decision.IGNORE;
    if (dx == 0) {
      return dy > 0 ? Decision.TOP : Decision.BOTTOM;
    }
    if (dy == 0) {
      return dx > 0 ? Decision.LEFT : Decision.RIGHT;
    }
    // We have to decide which border of rect was hit first (interesting case is when dx != 0 and dy != 0.
    float adx = Math.abs(dx);
    float ady = Math.abs(dy);
    // Distance to collision before this step.
    float distanceY = 0;
    if (dy > 0) {
      distanceY = rect.getY() - GeometryUtils.getMaxY(player) + ady;
    } else {
      distanceY = player.getY() - GeometryUtils.getMaxY(rect) + ady;
    }
    if (distanceY < 0) {
      return dx < 0 ? Decision.RIGHT : Decision.LEFT;
    }
    float distanceX = 0;
    if (dx > 0) {
      distanceX = rect.getX() - GeometryUtils.getMaxX(player) + adx;
    } else {
      distanceX = player.getX() - GeometryUtils.getMaxX(rect) + adx;
    }
    if (distanceX < 0) {
      return dy < 0 ? Decision.BOTTOM : Decision.TOP;
    }
    // Now we should check which of abs(distanceX/dx) or abs(distanceY/dy) is smaller. Unfortunately this would lead to division
    // by zero so we check distanceX * dy vs distanceY * dx instead. We also cheat on abs.
    if (distanceX * ady < distanceY * adx) {
      // X axis would hit first.
      return dx > 0 ? Decision.LEFT : Decision.RIGHT;
    } else {
      return dy > 0 ? Decision.TOP : Decision.BOTTOM;
    }
  }
}
