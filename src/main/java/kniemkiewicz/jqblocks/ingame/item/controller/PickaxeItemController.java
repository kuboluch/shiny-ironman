package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.item.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundFactory;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class PickaxeItemController extends AbstractActionItemController<PickaxeItem> {
  public static Log logger = LogFactory.getLog(PickaxeItemController.class);

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  BackgroundFactory backgroundFactory;

  private DigEffect digEffect;

  private int blockEndurance = 0;

  @Override
  boolean canPerformAction(int x, int y) {
    return getBlock(new Rectangle(x, y, 1, 1)) != null;
  }

  @Override
  Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK - 1, Sizes.BLOCK - 1);
  }

  @Override
  void startAction(PickaxeItem item) {
    AbstractBlock block = getAffectedBlock();
    blockEndurance = block.getEndurance();
    digEffect = new DigEffect(blockEndurance, affectedRectangle);
    renderQueue.add(digEffect);
  }

  @Override
  void stopAction(PickaxeItem item) {
    renderQueue.remove(digEffect);
    digEffect = null;
    blockEndurance = 0;
  }

  @Override
  void updateAction(PickaxeItem item, int delta) {
    blockEndurance -= delta * item.getStrength();
    digEffect.setCurrentEndurance(blockEndurance);
  }

  @Override
  boolean isActionCompleted() {
    return blockEndurance <= 0;
  }

  @Override
  void onAction() {
    AbstractBlock block = getAffectedBlock();
    if (block != null) {
      removeBlock(block);
    }
  }

  private AbstractBlock getAffectedBlock() {
    return getBlock(affectedRectangle);
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

  private void removeBlock(AbstractBlock block) {
    block.removeRect(affectedRectangle, blocks);
    backgrounds.add(backgroundFactory.getNaturalDirtBackground(
        affectedRectangle.getX(), affectedRectangle.getY(), affectedRectangle.getWidth(), affectedRectangle.getHeight()));
    assert !blocks.intersects(affectedRectangle).hasNext();
  }

  @Override
  public Shape getDropObjectShape(PickaxeItem item, int centerX, int centerY) {
    return null;
  }

  @Override
  public MovingPhysicalObject getDropObject(PickaxeItem item, int centerX, int centerY) {
    return null;
  }
}
