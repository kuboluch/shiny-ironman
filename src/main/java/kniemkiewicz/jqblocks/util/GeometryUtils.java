package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.ingame.hud.info.TimingInfo;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/21/12
 */
public final class GeometryUtils {

  // This will replace 1 in many places in this class and in whole project.
  static final float EPSILON = 0.0001f;

  public static Rectangle getBoundingRectangle(Shape shape) {
    Rectangle r = getBoundingRectangleInternal(shape);
    assert intersects(shape, r);
    return r;
  }

  static Rectangle getBoundingRectangleInternal(Shape shape) {
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
      return new Rectangle(x1 - EPSILON, y1 - EPSILON, x2 - x1 + 1 + EPSILON, y2 - y1 + 1 + EPSILON);
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
    Rectangle r1 = getBoundingRectangleInternal(shape1);
    Rectangle r2 = getBoundingRectangleInternal(shape2);

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
    if ((shape1 instanceof Rectangle && shape2 instanceof Circle) ||
        (shape2 instanceof Rectangle && shape1 instanceof Circle)) {
      Circle c;
      Rectangle r;
      if (shape1 instanceof Circle) {
        c = (Circle) shape1;
        r = (Rectangle) shape2;
      } else {
        c = (Circle) shape2;
        r = (Rectangle) shape1;
      }
      if (r.contains(c.getCenterX(), c.getCenterY())) {
        return true;
      }
    }
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

  public static float getMaxX(Shape rectangle) {
    return rectangle.getX() + rectangle.getWidth() - 1;
  }

  public static float getMaxY(Shape rectangle) {
    return rectangle.getY() + rectangle.getHeight() - 1;
  }

  public static Rectangle getRectangleCenteredOn(Rectangle rect, float margin) {
    return getRectangleCenteredOn(rect, rect.getWidth() + 2 * margin, rect.getHeight() + 2 * margin);
  }

  public static Rectangle getRectangleCenteredOn(Shape shape, float width, float height) {
    Rectangle rect = getNewBoundingRectangle(shape);
    rect.setX(rect.getX() - width / 2 + rect.getWidth() / 2);
    rect.setY(rect.getY() - height / 2 + rect.getHeight() / 2);
    rect.setWidth(width);
    rect.setHeight(height);
    return rect;
  }

  public static Line getLineInterval(Line line, float start, float end) {
    assert start >=0 && start <= 1;
    assert end >= 0 && end <= 1;
    return new Line(line.getX1() + line.getDX() * start, line.getY1() + line.getDY() * start,
        line.getX1() + line.getDX() * end, line.getY1() + line.getDY() * end);
  }

  public static Rectangle getRectangleCenteredOn(float x, float y, int size) {
    return new Rectangle(x - size / 2, y - size / 2, size, size);
  }


  public static boolean lineEquals(Line l1, Line l2) {
    return l1.getX1() == l2.getX1() && l1.getY1() == l2.getY1() && l1.getX2() == l2.getX2() && l1.getY2() == l2.getY2();
  }
}
