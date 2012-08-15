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

  public static Rectangle getOpenBoundingRectangle(Shape shape) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    rect.setX(rect.getX() + 1);
    rect.setY(rect.getY() + 1);
    rect.setHeight(rect.getWidth() - 2);
    rect.setHeight(rect.getHeight() - 2);
    return rect;
  }

  public static boolean intersects(Shape shape1, Shape shape2) {
    if (!internalIntersects(shape1, shape2)) return false;
    // getX + width is incorrect for lines
    if (shape1.getMaxX() < shape2.getMinX()) return false;
    if (shape2.getMaxX() < shape1.getMinX()) return false;
    if (shape1.getMaxY() < shape2.getMinY()) return false;
    if (shape2.getMaxY() < shape1.getMinY()) return false;
    return true;
  }

  public static boolean internalIntersects(Shape shape1, Shape shape2) {
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
