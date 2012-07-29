package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.item.DirtBlockItem;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.block.DirtBlock;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public class DirtBlockItemController extends AbstractActionItemController<DirtBlockItem> {
  @Autowired
  SolidBlocks blocks;

  @Override
  boolean canPerformAction(int x, int y) {
    return getBlock(new Rectangle(x, y, 1, 1)) == null;
  }

  @Override
  Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK - 1, Sizes.BLOCK - 1);
  }

  @Override
  void startAction(DirtBlockItem item) {  }

  @Override
  void stopAction(DirtBlockItem item) {  }

  @Override
  void updateAction(DirtBlockItem item, int delta) { }

  @Override
  boolean isActionCompleted() {
    return true;
  }

  @Override
  void onAction() {
    addBlock(affectedRectangle.getX(), affectedRectangle.getY());
  }

  private void addBlock(float x, float y) {
    addBlock(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y));
  }

  private void addBlock(int x, int y) {
    Rectangle rect = new Rectangle(x, y, Sizes.BLOCK - 1, Sizes.BLOCK - 1);
    AbstractBlock block = getBlock(rect);
    if (block == null) {
      DirtBlock newBlock = new DirtBlock(x, y, Sizes.BLOCK, Sizes.BLOCK);
      blocks.add(newBlock);
    }
  }

  private AbstractBlock getBlock(Rectangle rect) {
    AbstractBlock block = null;
    Iterator<AbstractBlock> it = blocks.intersects(rect);
    if (it.hasNext()) {
      block = it.next();
    }
    assert !it.hasNext();
    return block;
  }

  @Override
  public MovingPhysicalObject getDropObject(DirtBlockItem item, int centerX, int centerY) {
    return null;
  }
}
