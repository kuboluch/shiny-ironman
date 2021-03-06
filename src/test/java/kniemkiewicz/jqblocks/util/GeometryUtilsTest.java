package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.util.slick.SimpleGame;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.Arrays;

/**
 * User: knie
 * Date: 7/21/12
 */
public class GeometryUtilsTest {
  @Test
  public void testRectangles() {
    // Simple case.
    Rectangle r1 = new Rectangle(0, 0, 100, 200);
    Rectangle r2 = new Rectangle(-100, - 100, 110, 150);
    Assert.assertTrue(GeometryUtils.intersects(r1, r2));
    // r1 contains r3
    Rectangle r3 = new Rectangle(10, 10, 10, 10);
    Assert.assertTrue(GeometryUtils.intersects(r1, r3));
    Rectangle r4 = new Rectangle(-20, -20, 10, 10);
    Assert.assertFalse(GeometryUtils.intersects(r1, r4));
  }

  @Test
  public void testLines() {
    // Simple case.
    Line l1 = new Line(0, 0, 100, 100);
    Line l2 = new Line(0, 100, 100, 0);
    Assert.assertTrue(GeometryUtils.intersects(l1, l2));
    // Small rectangle
    Rectangle r1 = new Rectangle(10, 10, 10, 10);
    Assert.assertTrue(GeometryUtils.intersects(l1, r1));
    Assert.assertFalse(GeometryUtils.intersects(l2, r1));
    // Large rectangle
    Rectangle r2 = new Rectangle(-10, -10, 1000, 1000);
    Assert.assertTrue(GeometryUtils.intersects(r2, l1));
    Assert.assertTrue(GeometryUtils.intersects(l2, r2));
    Line l3 = new Line(10, 0, 110, 100);
    Assert.assertFalse(GeometryUtils.intersects(l1, l3));
    Assert.assertTrue(GeometryUtils.intersects(l2, l3));
  }

  @Test
  public void testBoundaryCaseForRectangles() {
    // Side by side
    Rectangle r1 = new Rectangle(0f, 0f, 100f, 100f);
    Rectangle r2 = new Rectangle(0f, 100f, 100f, 100f);
    Assert.assertFalse(GeometryUtils.intersects(r1, r2));

    Rectangle r3 = new Rectangle(0f, 0f, 100f, 100f);
    Rectangle r4 = new Rectangle(0f, 100.5f, 100f, 100f);
    Assert.assertFalse(GeometryUtils.intersects(r3, r4));

    Rectangle r5 = new Rectangle(0f, 0f, 100f, 100f);
    Rectangle r6 = new Rectangle(0f, 98.9f, 100f, 100f);
    Assert.assertTrue(GeometryUtils.intersects(r5, r6));
  }

  @Test
  public void testBoundaryCaseForCircles() {
    // Side by side
    Shape c1 = new Circle(0 , 0, 10);
    Shape c2 = new Circle(0 , 20, 10);
    Assert.assertFalse(GeometryUtils.intersects(c1, c2));

    Shape c3 = new Circle(19, 0, 10);
    Assert.assertTrue(GeometryUtils.intersects(c1, c3));
    Assert.assertFalse(GeometryUtils.intersects(c2, c3));
  }

  @Test
  public void testBoundingRectForCircles() {
    Circle c1 = new Circle(3, 3, 30);
    Rectangle r1 = GeometryUtils.getBoundingRectangleInternal(c1);
    Assert.assertTrue(GeometryUtils.intersects(c1, r1));

    Circle c2 = new Circle(3, 3, 3);
    Rectangle r2 = GeometryUtils.getBoundingRectangleInternal(c2);
    Assert.assertTrue(GeometryUtils.intersects(c1, r2));

    // One more case, which is not covered by fix checking if center of circle is inside rect.
    Rectangle r3 = new Rectangle(-100, 5, 200, 10);
    Assert.assertTrue(GeometryUtils.intersects(c1, r3));
  }

  @Test
  public void testBoundingRectForLines() {
    Line l1 = new Line(-72,1043, 288,1043);
    Rectangle r1 = GeometryUtils.getBoundingRectangleInternal(l1);
    Assert.assertTrue(GeometryUtils.intersects(l1, r1));
  }
}