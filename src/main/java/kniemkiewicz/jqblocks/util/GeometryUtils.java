package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/21/12
 */
public final class GeometryUtils {
  public static Rectangle getBoundingRectangle(Shape shape) {
    if (shape instanceof Rectangle) return (Rectangle)shape;
    return getNewBoundingRectangle(shape);
  }
  public static Rectangle getNewBoundingRectangle(Shape shape) {
    return new Rectangle(shape.getMinX(), shape.getMinY(), shape.getWidth(), shape.getHeight());
  }
  public static boolean intersects(Shape shape1, Shape shape2) {
    TimingInfo.CURRENT_TIMING_INFO.getCounter("GeometryUtils.intersects").record(1);
    if (shape1.intersects(shape2)) return true;

    if (shape2 instanceof Line) {
      return shape1.contains(shape2);
    }
    if (shape1 instanceof Line) {
      return shape2.contains(shape1);
    }
    return false;
  }
}
