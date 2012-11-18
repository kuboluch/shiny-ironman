package kniemkiewicz.jqblocks.ingame.util.movement;

import kniemkiewicz.jqblocks.ingame.Sizes;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class SingleAxisMovement implements Serializable{

  private static final long serialVersionUID = 1;

  float pos;
  float speed;
  float acceleration = 0;
  // True means it is facing towards positive infinity.
  boolean direction = true;
  final MovementDefinition definition;


  SingleAxisMovement(float speed, float x, MovementDefinition definition) {
    this.speed = speed;
    this.pos = x;
    this.definition = definition;
    if (definition.autoDirection) {
      direction = (speed >= 0);
    }
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

  final public void update(int unscaledDelta) {
    float delta = unscaledDelta / Sizes.TIME_UNIT;
    if (acceleration != 0) {
      speed += acceleration * delta;
      acceleration = 0;
    } else {
      if (Math.abs(speed) < definition.defaultDeceleration * delta) {
        speed = 0;
      } else if (speed > 0) {
        speed -= definition.defaultDeceleration * delta;
      } else {
        speed += definition.defaultDeceleration * delta;
      }
    }
    if (definition.autoDirection) {
      if (speed != 0) {
        direction = speed >= 0;
      }
    }
    if (direction) {
      if (speed < - definition.maxSpeedBackward) {
        speed = - definition.maxSpeedBackward;
      } else if (speed > definition.maxSpeedForward) {
        speed = definition.maxSpeedForward;
      }
    } else {
      if (speed < - definition.maxSpeedForward) {
        speed = - definition.maxSpeedForward;
      } else if (speed > definition.maxSpeedBackward) {
        speed = definition.maxSpeedBackward;
      }
    }
    pos += speed * delta;
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

  final public boolean getDirection() {
    return direction;
  }

  final public void setDirection(boolean  b) {
    this.direction = b;
  }

  final public void acceleratePositive() {
    assert definition.maxSpeedForward != Float.MAX_VALUE;
    assert definition.maxSpeedBackward != Float.MAX_VALUE;
    if (direction) {
      acceleration = definition.maxSpeedForward / definition.defaultAccelerationTime;
    } else {
      acceleration = definition.maxSpeedBackward / definition.defaultAccelerationTime;
    }
  }

  final public void accelerateNegative() {
    assert definition.maxSpeedForward != Float.MAX_VALUE;
    assert definition.maxSpeedBackward != Float.MAX_VALUE;
    if (direction) {
      acceleration = - definition.maxSpeedBackward / definition.defaultAccelerationTime;
    } else {
      acceleration = - definition.maxSpeedForward / definition.defaultAccelerationTime;
    }
  }
}
