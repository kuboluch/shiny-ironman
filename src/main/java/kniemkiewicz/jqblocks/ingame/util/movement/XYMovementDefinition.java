package kniemkiewicz.jqblocks.ingame.util.movement;

import java.io.Serializable;

/**
 * User: knie
 * Date: 8/30/12
 */
// TODO: do it in such a way that serialization does not make billion copies of this class
public class XYMovementDefinition implements Serializable {

  public final static XYMovementDefinition STATIONARY = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(0),
      new MovementDefinition().setMaxSpeed(0)
  );

  final MovementDefinition xDefinition;
  final MovementDefinition yDefinition;

  public XYMovementDefinition(MovementDefinition xDefinition, MovementDefinition yDefinition) {
    this.xDefinition = xDefinition;
    this.yDefinition = yDefinition;
  }

  public XYMovement getMovement(float x, float y) {
    return new XYMovement(xDefinition.getMovement(x, 0), yDefinition.getMovement(y, 0));
  }

  public XYMovement getMovement(XYMovement movement) {
    XYMovement newMovement = this.getMovement(movement.getX(), movement.getY());
    newMovement.setXSpeed(movement.getXMovement().getSpeed());
    newMovement.setYSpeed(movement.getYMovement().getSpeed());
    newMovement.getXMovement().setDirection(movement.getXMovement().getDirection());
    newMovement.getYMovement().setDirection(movement.getYMovement().getDirection());
    return newMovement;
  }
}
