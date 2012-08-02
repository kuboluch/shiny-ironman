package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.geom.Shape;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public interface PhysicalObject extends Serializable, QuadTree.HasShape {
  Shape getShape();
}
