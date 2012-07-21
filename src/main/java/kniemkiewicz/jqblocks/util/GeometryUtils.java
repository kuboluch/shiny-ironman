package kniemkiewicz.jqblocks.util;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/21/12
 */
public class GeometryUtils {
  public static Rectangle getBoundingRectangle(Shape shape) {
    return new Rectangle(shape.getMinX(), shape.getMinY(), shape.getWidth(), shape.getHeight());
  }
}
