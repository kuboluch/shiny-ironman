package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.item.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.object.DigEffect;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundFactory;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
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
    return blocks.getBlocks().getValueForUnscaledPoint(x, y) == WallBlockType.DIRT;
  }

  WallBlockType getAffectedBlock(int x, int y) {
    return blocks.getBlocks().getValueForUnscaledPoint(x, y);
  }

  @Override
  Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
  }

  @Override
  void startAction(PickaxeItem item) {
    WallBlockType block = getAffectedBlock();
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
    if (affectedRectangle != null) {
      removeBlock();
    }
  }

  private WallBlockType getAffectedBlock() {
    return getAffectedBlock((int)affectedRectangle.getX(), (int)affectedRectangle.getY());
  }

  private void removeBlock() {
    blocks.getBlocks().setRectUnscaled(affectedRectangle, WallBlockType.EMPTY);
    backgrounds.add(backgroundFactory.getNaturalDirtBackground(
        affectedRectangle.getX(), affectedRectangle.getY(), affectedRectangle.getWidth(), affectedRectangle.getHeight()));
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
