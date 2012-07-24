package kniemkiewicz.jqblocks.util;

import org.junit.Test;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.junit.Assert;

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
}