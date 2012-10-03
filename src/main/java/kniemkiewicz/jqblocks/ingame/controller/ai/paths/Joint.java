package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.util.Assert;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 28.09.12
 */
public class Joint implements Serializable{
  private Joint other;
  final private float position; // 0 to 1, along the edge that joint belong to, not the one stored inside
  final private Edge edge;

  public Joint(float position, Edge edge) {
    assert position <= 1 && position >= 0;
    this.position = position;
    this.edge = edge;
  }

  // This should be called just after constructor.
  void with(Joint other) {
    this.other = other;
    other.other = this;
  }

  public Joint getOther() {
    assert other != null;
    return other;
  }

  public float getPosition() {
    assert other != null;
    return position;
  }

  public Edge getEdge() {
    assert other != null;
    return edge;
  }
}
