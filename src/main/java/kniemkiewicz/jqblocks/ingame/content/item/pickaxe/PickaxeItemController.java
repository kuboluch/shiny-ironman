package kniemkiewicz.jqblocks.ingame.content.item.pickaxe;

import kniemkiewicz.jqblocks.ingame.FreeFallController;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.item.controller.AbstractActionItemController;
import kniemkiewicz.jqblocks.ingame.object.DigEffect;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.NaturalDirtBackground;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickaxeItemController extends AbstractActionItemController<PickaxeItem> {

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  FreeFallController freeFallController;

  private DigEffect digEffect;

  private int blockEndurance = 0;

  @Override
  protected boolean canPerformAction(int x, int y) {
    return blocks.getBlocks().getValueForUnscaledPoint(x, y) == WallBlockType.DIRT;
  }

  WallBlockType getAffectedBlock(int x, int y) {
    return blocks.getBlocks().getValueForUnscaledPoint(x, y);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
  }

  @Override
  protected void startAction(PickaxeItem item) {
    WallBlockType block = getAffectedBlock();
    blockEndurance = block.getEndurance();
    digEffect = new DigEffect(blockEndurance, affectedRectangle);
    renderQueue.add(digEffect);
  }

  @Override
  protected void stopAction(PickaxeItem item) {
    renderQueue.remove(digEffect);
    digEffect = null;
    blockEndurance = 0;
  }

  @Override
  protected void updateAction(PickaxeItem item, int delta) {
    blockEndurance -= delta * item.getStrength();
    digEffect.setCurrentEndurance(blockEndurance);
  }

  @Override
  protected boolean isActionCompleted() {
    return blockEndurance <= 0;
  }

  @Override
  protected void onAction() {
    if (affectedRectangle != null) {
      removeBlock();
    }
  }

  private WallBlockType getAffectedBlock() {
    return getAffectedBlock((int)affectedRectangle.getX(), (int)affectedRectangle.getY());
  }

  private void removeBlock() {
    blocks.getBlocks().setRectUnscaled(affectedRectangle, WallBlockType.EMPTY);
    backgrounds.add(new NaturalDirtBackground(
        affectedRectangle.getX(), affectedRectangle.getY(), affectedRectangle.getWidth(), affectedRectangle.getHeight()));
    Rectangle rect = new Rectangle(affectedRectangle.getX(), affectedRectangle.getY() - Sizes.BLOCK / 4,
        affectedRectangle.getWidth(), affectedRectangle.getHeight());
    freeFallController.addObjectsInRectangle(rect);
  }

  @Override
  public DroppableObject getObject(PickaxeItem item, int centerX, int centerY) {
    return null;
  }
}