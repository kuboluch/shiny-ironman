package kniemkiewicz.jqblocks.ingame.util;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public class HorizontalMovement implements Serializable{
  float y;
  final SingleAxisMovement xMovement;

  public HorizontalMovement(float x, float y, float maxXSpeed) {
    this.xMovement = new SingleAxisMovement(maxXSpeed, 0, x, 0);
    this.y = y;
  }

  public SingleAxisMovement getXMovement() {
    return xMovement;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getX() {
    return xMovement.getPos();
  }

  public void update(int delta) {
    xMovement.update(delta);
  }
}
