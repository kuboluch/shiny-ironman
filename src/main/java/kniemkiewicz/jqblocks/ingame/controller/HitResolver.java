package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.Player;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 17.07.12
 */
public class HitResolver {
  static
  enum Decision {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    IGNORE
  }

  /**
   * TODO: This should be implemented as a common operation on all moving objects
   * Player.getShape has to be partially inside rect. dx and dy give the direction of recent player movement.
   */
  public static void resolve(Player player, float dx, float dy, Rectangle rect) {
    if (!player.getShape().intersects(rect)) return;
    Decision decision = decide(player.getShape(), dx, dy, rect);
    switch (decision) {
      case TOP:
        player.getYMovement().setSpeed(0);
        player.getYMovement().setPos(rect.getMinY() - Player.HEIGHT);
        break;
      case BOTTOM:
        player.getYMovement().setSpeed(0);
        player.getYMovement().setPos(rect.getMaxY() + 2);
        break;
      case LEFT:
        player.getXMovement().setSpeed(0);
        player.getXMovement().setPos(rect.getMinX() - Player.WIDTH);
        break;
      case RIGHT:
        player.getXMovement().setSpeed(0);
        player.getXMovement().setPos(rect.getMaxX() + 2);
        break;
      case IGNORE:
        break;
    }
    assert player.getXMovement().getPos() > Sizes.MIN_X;
    assert player.getXMovement().getPos() < Sizes.MAX_X + 100;
    assert player.getYMovement().getPos() > Sizes.MIN_Y;
    assert player.getYMovement().getPos() < Sizes.MAX_Y + 100;
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
      distanceY = rect.getMinY() - player.getMaxY() + ady;
    } else {
      distanceY = player.getMinY() - rect.getMaxY() + ady;
    }
    if (distanceY < 0) {
      return dx < 0 ? Decision.RIGHT : Decision.LEFT;
    }
    float distanceX = 0;
    if (dx > 0) {
      distanceX = rect.getMinX() - player.getMaxX() + adx;
    } else {
      distanceX = player.getMinX() - rect.getMaxX() + adx;
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
