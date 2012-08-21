package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.content.block.dirt.DirtBlockItem;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public class DirtBlockItemController extends AbstractActionItemController<DirtBlockItem> {
  @Autowired
  SolidBlocks blocks;

  @Override
  protected boolean canPerformAction(int x, int y) {
    return !blocks.getBlocks().collidesWithNonEmpty(new Rectangle(x, y, 1, 1));
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
  }

  @Override
  protected void startAction(DirtBlockItem item) { }

  @Override
  protected void stopAction(DirtBlockItem item) { }

  @Override
  protected void updateAction(DirtBlockItem item, int delta) { }

  @Override
  protected boolean isActionCompleted() {
    return true;
  }

  @Override
  protected void onAction() {
    addBlock(affectedRectangle.getX(), affectedRectangle.getY());
  }

  private void addBlock(float x, float y) {
    addBlock(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y));
  }

  private void addBlock(int x, int y) {
    Rectangle rect = new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
    if (!blocks.getBlocks().collidesWithNonEmpty(rect)) {
      blocks.add(new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK), WallBlockType.DIRT);
    }
  }

  @Override
  public DroppableObject getObject(DirtBlockItem item, int centerX, int centerY) {
    return null;
  }
}
