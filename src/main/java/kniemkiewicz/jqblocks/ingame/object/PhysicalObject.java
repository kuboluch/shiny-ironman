package kniemkiewicz.jqblocks.ingame.object;

import org.newdawn.slick.geom.Shape;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public interface PhysicalObject extends Serializable {
  Shape getShape();
}
