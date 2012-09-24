package kniemkiewicz.jqblocks.ingame.util.movement;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

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

  public void applyAcceleration(Vector2f accelerationVector) {
    xMovement.setAcceleration(accelerationVector.getX());
    yMovement.setAcceleration(accelerationVector.getY());
  }

  final public Vector2f getSpeed() {
    return new Vector2f(xMovement.getSpeed(), yMovement.getSpeed());
  }

  final public void defaultUpdateShape(Shape shape) {
    shape.setX(this.getX());
    shape.setY(this.getY());
  }
}
