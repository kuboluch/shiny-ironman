package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.Sizes;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: qba
 * Date: 11.08.12
 */
public class SolidBlocksTest {

  @Test
  public void onSolidGroundTest() {
    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int length = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    RawEnumTable table = new RawEnumTable<WallBlockType>(WallBlockType.EMPTY, WallBlockType.SPACE, width, length);

    Rectangle floor = new Rectangle(- (10 * Sizes.BLOCK), 10 * Sizes.BLOCK, (10 * Sizes.BLOCK) * 2, Sizes.BLOCK);
    table.setRectUnscaled(floor, WallBlockType.DIRT);

    SolidBlocks blocks = new SolidBlocks(table);

    Rectangle test = new Rectangle(0, floor.getY() - (2 * Sizes.BLOCK), 2 * Sizes.BLOCK, 2 * Sizes.BLOCK);
    Assert.assertTrue("Test.(y + height) should be same as floor.y", test.getY() + test.getHeight() == floor.getY());
    Assert.assertTrue("Should be on solid ground when test.(y + height) same as floor.y", blocks.isOnSolidGround(test));

    test.setY(floor.getY() - (2 * Sizes.BLOCK) - (Sizes.BLOCK * 0.5f));
    Assert.assertTrue("Test.(y + height) should be half block above floor.y", test.getY() + test.getHeight() == floor.getY() - (Sizes.BLOCK * 0.5f));
    Assert.assertFalse("Should not be on solid ground when test.(y + height) is half block above floor.y", blocks.isOnSolidGround(test));

    test.setY(floor.getY() - (2 * Sizes.BLOCK) + (Sizes.BLOCK * 0.5f));
    Assert.assertTrue("Test.(y + height) should be half block below floor.y", test.getY() + test.getHeight() == floor.getY() + (Sizes.BLOCK * 0.5f));
    Assert.assertFalse("Should not be on solid ground when test.(y + height) is below block above floor.y", blocks.isOnSolidGround(test));

    Rectangle hole = new Rectangle(Sizes.BLOCK, 10 * Sizes.BLOCK, Sizes.BLOCK, Sizes.BLOCK);
    blocks.getBlocks().setRectUnscaled(hole, WallBlockType.EMPTY);

    test.setY(floor.getY() - (2 * Sizes.BLOCK));
    Assert.assertTrue("Test.(y + height) should be same as floor.y", test.getY() + test.getHeight() == floor.getY());
    Assert.assertFalse("Should not be on solid ground when floor has hole ", blocks.isOnSolidGround(test));

    blocks.getBlocks().setRectUnscaled(hole, WallBlockType.DIRT);

    Rectangle pile = new Rectangle(Sizes.BLOCK, (10 * Sizes.BLOCK) - Sizes.BLOCK, Sizes.BLOCK, Sizes.BLOCK);
    blocks.getBlocks().setRectUnscaled(pile, WallBlockType.DIRT);
    Assert.assertFalse("Should not be on solid ground when floor has pile ", blocks.isOnSolidGround(test));

    blocks.getBlocks().setRectUnscaled(hole, WallBlockType.EMPTY);

    Rectangle floatingRock = new Rectangle(0, (10 * Sizes.BLOCK) - (2 * Sizes.BLOCK), Sizes.BLOCK, Sizes.BLOCK);
    blocks.getBlocks().setRectUnscaled(floatingRock, WallBlockType.DIRT);
    Assert.assertFalse("Should not be on solid ground when there is floatingRock ", blocks.isOnSolidGround(test));
  }
}
