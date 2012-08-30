package kniemkiewicz.jqblocks.ingame.util.movement;

import java.io.Serializable;

/**
 * User: knie
 * Date: 8/30/12
 */
// TODO: do it in such a way that serialization does not make billion copies of this class
public class XYMovementDefinition  implements Serializable {

  final MovementDefinition xDefinition;
  final MovementDefinition yDefinition;

  public XYMovementDefinition(MovementDefinition xDefinition, MovementDefinition yDefinition) {
    this.xDefinition = xDefinition;
    this.yDefinition = yDefinition;
  }

  public XYMovement getMovement(float x, float y) {
    return new XYMovement(xDefinition.getMovement(x, 0), yDefinition.getMovement(y, 0));
  }
}
