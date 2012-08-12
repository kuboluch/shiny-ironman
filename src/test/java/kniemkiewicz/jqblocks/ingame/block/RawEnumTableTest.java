package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.Sizes;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: qba
 * Date: 11.08.12
 */
public class RawEnumTableTest {

  @Test
  public void playerTest() {
    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int length = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    RawEnumTable table = new RawEnumTable<WallBlockType>(WallBlockType.EMPTY, WallBlockType.SPACE, width, length);

    Rectangle floor = new Rectangle(- (10 * Sizes.BLOCK), 10 * Sizes.BLOCK, (10 * Sizes.BLOCK) * 2, Sizes.BLOCK);
    table.setRectUnscaled(floor, WallBlockType.DIRT);

    Rectangle test = new Rectangle(0, floor.getY() - Sizes.BLOCK - 1f, Sizes.BLOCK, Sizes.BLOCK);
    Assert.assertTrue("Test.(y + height) should be 1 above floor.y", test.getY() + test.getHeight() == floor.getY() - 1f);
    Assert.assertFalse("Should not collide when test.(y + height) is 1 above floor.y", table.collidesWithNonEmpty(test));
    Assert.assertNotNull("Should not be null", table.getIntersectingRectangles(test));
    Assert.assertTrue("Should be empty when test.(y + height) is 1 above floor.y", table.getIntersectingRectangles(test).isEmpty());

    test.setY(floor.getY() - Sizes.BLOCK - 0.5f);
    Assert.assertTrue("Test.maxY should be 0.5 above floor.minY", test.getY() + test.getHeight() == floor.getY() - 0.5f);
    Assert.assertFalse("Should not collide when test.(y + height) is 0.5 above floor.y", table.collidesWithNonEmpty(test));
    Assert.assertNotNull("Should not be null", table.getIntersectingRectangles(test));
    Assert.assertTrue("Should be empty when test.(y + height) is 0.5 above floor.y", table.getIntersectingRectangles(test).isEmpty());

    test.setY(floor.getY()- Sizes.BLOCK);
    Assert.assertTrue("Test.maxY should be same as floor.minY", test.getY() + test.getHeight() == floor.getY());
    Assert.assertFalse("Should not collide when test.(y + height) same as floor.y", table.collidesWithNonEmpty(test));
    Assert.assertNotNull("Should not be null", table.getIntersectingRectangles(test));
    Assert.assertTrue("Should be empty when test.(y + height) same as floor.y", table.getIntersectingRectangles(test).isEmpty());

    test.setY(floor.getY() - Sizes.BLOCK + 0.5f);
    Assert.assertTrue("Test.maxY should be 0.5 below floor.minY", test.getY() + test.getHeight() == floor.getY() + 0.5f);
    Assert.assertFalse("Should not collide when test.(y + height) is 0.5 below floor.y", table.collidesWithNonEmpty(test));
    Assert.assertNotNull("Should not be null", table.getIntersectingRectangles(test));
    Assert.assertTrue("Should be empty when test.(y + height) is 0.5 below floor.y", table.getIntersectingRectangles(test).isEmpty());

    test.setY(floor.getY() - Sizes.BLOCK + 1f);
    Assert.assertTrue("Test.(y + height) should be 1 below floor.minY", test.getY() + test.getHeight() == floor.getY() + 1f);
    Assert.assertTrue("Should collide when test.(y + height) is 1 below floor.y", table.collidesWithNonEmpty(test));
    Assert.assertNotNull("Should not be null", table.getIntersectingRectangles(test));
    Assert.assertFalse("Should be empty when test.(y + height) is 1 below floor.y", table.getIntersectingRectangles(test).isEmpty());
  }
}
