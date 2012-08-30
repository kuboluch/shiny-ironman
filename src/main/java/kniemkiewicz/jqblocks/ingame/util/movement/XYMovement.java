package kniemkiewicz.jqblocks.ingame.util.movement;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public class XYMovement implements Serializable {
  final SingleAxisMovement xMovement;
  final SingleAxisMovement yMovement;


  XYMovement(SingleAxisMovement xMovement, SingleAxisMovement yMovement) {
    this.xMovement = xMovement;
    this.yMovement = yMovement;
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

  final public XYMovement setXSpeed(float speed) {
    xMovement.setSpeed(speed);
    return this;
  }

  final public XYMovement setYSpeed(float speed) {
    yMovement.setSpeed(speed);
    return this;
  }

  public void update(int delta) {
    xMovement.update(delta);
    yMovement.update(delta);
  }
}
