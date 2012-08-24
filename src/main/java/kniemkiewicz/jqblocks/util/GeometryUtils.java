package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import org.newdawn.slick.geom.Circle;
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
    if (shape instanceof Circle) {
      Circle circle = (Circle) shape;
      float x = circle.getCenterX();
      float y = circle.getCenterY();
      float r = circle.getRadius();
      return new Rectangle(x - r, y - r, 2 * r, 2 * r);
    }
    if (shape instanceof Line) {
      Line line = (Line) shape;
      float x1;
      float x2;
      if (line.getX2() > line.getX1()) {
        x1 = line.getX1();
        x2 = line.getX2();
      } else {
        x1 = line.getX2();
        x2 = line.getX1();
      }
      float y1;
      float y2;
      if (line.getY2() > line.getY1()) {
        y1 = line.getY1();
        y2 = line.getY2();
      } else {
        y1 = line.getY2();
        y2 = line.getY1();
      }
      return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }
    if (shape instanceof Rectangle) {
      Rectangle r = (Rectangle) shape;
      return new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
    assert false;
    return new Rectangle(shape.getMinX(), shape.getMinY(), shape.getWidth(), shape.getHeight());
  }

  public static boolean intersects(Shape shape1, Shape shape2) {
    if (!internalIntersects(shape1, shape2)) return false;
    // This has to be made faster...
    Rectangle r1 = getBoundingRectangle(shape1);
    Rectangle r2 = getBoundingRectangle(shape2);

    // getX + width is incorrect for lines
    if (getMaxX(r1) < r2.getX()) return false;
    if (getMaxX(r2) < r1.getX()) return false;
    if (getMaxY(r1) < r2.getY()) return false;
    if (getMaxY(r2) < r1.getY()) return false;
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

  public static double distance(float x1, float y1, float x2, float y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

  public static String toString(Rectangle rect) {
    return "Rect{"+ rect.getX() + "," + rect.getY() + "," + rect.getMaxX() + "," + rect.getMaxY() + "}";
  }

  public static float getMaxX(Rectangle rectangle) {
    return rectangle.getX() + rectangle.getWidth() - 1;
  }

  public static float getMaxY(Rectangle rectangle) {
    return rectangle.getY() + rectangle.getHeight() - 1;
  }

}
