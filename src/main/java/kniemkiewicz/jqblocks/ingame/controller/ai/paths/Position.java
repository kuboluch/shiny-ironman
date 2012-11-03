package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import org.apache.commons.lang.ObjectUtils;
import org.newdawn.slick.geom.Vector2f;

/**
* User: krzysiek
* Date: 23.10.12
*/
final public class Position {
  Edge e;
  float j;

  public Position(Edge e, float j) {
    this.e = e;
    this.j = j;
  }

  public Edge getEdge() {
    return e;
  }

  public float getPosition() {
    return j;
  }

  public void setEdge(Edge e) {
    this.e = e;
  }

  public void setPosition(float j) {
    this.j = j;
  }

  public Vector2f getPoint() {
    return e.getPointFor(j);
  }

  @Override
  public String toString() {
    return "Position{" + ObjectUtils.toString(e) + "," + j + "}";
  }
}
