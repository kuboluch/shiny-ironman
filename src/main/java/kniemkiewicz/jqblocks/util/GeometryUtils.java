package kniemkiewicz.jqblocks.util;

import org.newdawn.slick.geom.Line;
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
  public static boolean intersects(Shape shape1, Shape shape2) {
    if (shape1.intersects(shape2)) return true;

    if (shape2 instanceof Line) {
      return shape1.contains(shape2);
    }
    return false;
  }
}
