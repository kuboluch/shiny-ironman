package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import com.google.common.base.Objects;
import kniemkiewicz.jqblocks.util.Assert;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 28.09.12
 */
final public class Joint implements Serializable {
  private Joint other;
  final private float position; // 0 to 1, along the edge that joint belong to, not the one stored inside
  final private Edge edge;

  public Joint(float position, Edge edge) {
    assert position <= 1 && position >= 0;
    this.position = position;
    this.edge = edge;
  }

  // This should be called just after constructor.
  Joint with(Joint other) {
    this.other = other;
    other.other = this;
    return this;
  }

  public Joint getOther() {
    return other;
  }

  public float getPosition() {
    return position;
  }

  public Edge getEdge() {
    return edge;
  }

  @Override
  public String toString() {
    return "Joint{" + ObjectUtils.toString(edge) + "," + position + "}";
  }
}
