package kniemkiewicz.jqblocks.ingame.util;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 19.08.12
 * TODO: interface for this and HorizontalMovement so that other classes do not have to care which ones is it.
 */
public class FullXYMovement implements Serializable {
  final SingleAxisMovement xMovement;
  final SingleAxisMovement yMovement;


  public FullXYMovement(float x, float y, float maxXSpeed, float maxYSpeed) {
    this.xMovement = new SingleAxisMovement(maxXSpeed, 0, x, 0);
    this.yMovement = new SingleAxisMovement(maxYSpeed, 0, y, 0);
  }

  final public SingleAxisMovement getXMovement() {
    return xMovement;
  }

  final public SingleAxisMovement getYMovement() {
    return yMovement;
  }

  // Some shortcuts.

  final public float getX() {
    return xMovement.getPos();
  }

  final public float getY() {
    return yMovement.getPos();
  }

  public void update(int delta) {
    xMovement.update(delta);
    yMovement.update(delta);
  }
}
