package kniemkiewicz.jqblocks.ingame.util.movement;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class SingleAxisMovement implements Serializable{

  private static final long serialVersionUID = 1;

  float pos;
  float speed;
  float maxSpeed;
  float acceleration = 0;
  float defaultDeceleration;
  boolean lastDirection;

  public SingleAxisMovement(float maxSpeed, float speed, float x, float defaultDeceleration) {
    this.maxSpeed = maxSpeed;
    this.speed = speed;
    this.pos = x;
    this.defaultDeceleration = defaultDeceleration;
    this.lastDirection = speed >= 0;
  }

  final public float getPos() {
    return pos;
  }

  final public float getSpeed() {
    return speed;
  }

  final public float getAcceleration() {
    return acceleration;
  }

  final public void setAcceleration(float acceleration) {
    this.acceleration = acceleration;
  }

  final public void setSpeed(float speed) {
    this.speed = speed;
  }

  final public void update(int delta) {
    if (acceleration != 0) {
      speed += acceleration * delta;
      acceleration = 0;
      if (speed > maxSpeed) {
        speed = maxSpeed;
      } else if (speed < -maxSpeed) {
        speed = -maxSpeed;  
      }
    } else {
      if (Math.abs(speed) < defaultDeceleration * delta) {
        speed = 0;
      } else if (speed > 0) {
        speed -= defaultDeceleration * delta;
      } else {
        speed += defaultDeceleration * delta;
      }
    }
    pos += speed * delta;
    if (speed != 0) {
      lastDirection = speed >= 0;
    }
  }

  final public void setPos(float pos) {
    this.pos = pos;
  }

  @Override
  final public String toString() {
    return "SingleAxisMovement{" +
        "x=" + pos +
        ", v=" + speed +
        ", a=" + acceleration +
        '}';
  }

  final public void limitSpeed(float currentMaxSpeed) {
    if (Math.abs(speed) > currentMaxSpeed) {
      speed = speed > 0 ? currentMaxSpeed : -currentMaxSpeed;
    }
  }

  final public boolean getLastDirection() {
    return lastDirection;
  }

  public void setDefaultDeceleration(float defaultDeceleration) {
    this.defaultDeceleration = defaultDeceleration;
  }
}
