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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Position position = (Position) o;

    if (Float.compare(position.j, j) != 0) return false;
    if (e != null ? !e.equals(position.e) : position.e != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = e != null ? e.hashCode() : 0;
    result = 31 * result + (j != +0.0f ? Float.floatToIntBits(j) : 0);
    return result;
  }
}
