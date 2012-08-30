package kniemkiewicz.jqblocks.ingame.util.movement;

import java.io.Serializable;

/**
 * User: knie
 * Date: 8/30/12
 */
// TODO: do it in such a way that serialization does not make billion copies of this class
public class MovementDefinition implements Serializable{
  float maxSpeedForward = Float.MAX_VALUE;
  float maxSpeedBackward = Float.MAX_VALUE;
  float defaultDeceleration = 0;
  float defaultAccelerationTime = 0;
  boolean autoDirection = true;

  public MovementDefinition() {  }

  public MovementDefinition setMaxSpeedForward(float maxSpeedForward) {
    this.maxSpeedForward = maxSpeedForward;
    return this;
  }

  public MovementDefinition setMaxSpeedBackward(float maxSpeedBackward) {
    this.maxSpeedBackward = maxSpeedBackward;
    return this;
  }

  public MovementDefinition setMaxSpeed(float speed) {
    this.maxSpeedBackward = speed;
    this.maxSpeedForward = speed;
    return this;
  }

  public MovementDefinition setDefaultDeceleration(float defaultDeceleration) {
    this.defaultDeceleration = defaultDeceleration;
    return this;
  }

  public MovementDefinition setDefaultAccelerationTime(float defaultAccelerationTime) {
    this.defaultAccelerationTime = defaultAccelerationTime;
    return this;
  }

  public MovementDefinition setAutoDirection(boolean autoDirection) {
    this.autoDirection = autoDirection;
    return this;
  }

  public SingleAxisMovement getMovement(float pos, float speed) {
    return new SingleAxisMovement(speed, pos, this);
  }
}
