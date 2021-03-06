package kniemkiewicz.jqblocks.ingame.content.item.pickaxe;

import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.inventory.item.controller.AbstractActionItemRectangleController;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.BackgroundBlockType;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.object.DigEffect;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;

@Component
public class PickaxeItemController extends AbstractActionItemRectangleController<PickaxeItem> {

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  FreeFallController freeFallController;

  @Resource
  Sound pickaxeSound;

  @Autowired
  SoundController soundController;

  private DigEffect digEffect;

  private int blockEndurance = 0;

  @Override
  protected boolean canPerformAction(int x, int y) {
    return (blocks.getBlocks().getValueForUnscaledPoint(x, y) == WallBlockType.DIRT) &&
        isDigAllowed(getAffectedRectangle(x, y));
  }

  WallBlockType getAffectedBlock(int x, int y) {
    return blocks.getBlocks().getValueForUnscaledPoint(x, y);
  }

  @Override
  protected Rectangle getAffectedRectangle(int x, int y) {
    return new Rectangle(x, y, Sizes.BLOCK, Sizes.BLOCK);
  }

  @Override
  protected void startAction() {
    WallBlockType block = getAffectedBlock();
    blockEndurance = block.getEndurance();
    digEffect = new DigEffect(blockEndurance, affectedRectangle);
    renderQueue.add(digEffect);
  }

  @Override
  protected void stopAction() {
    renderQueue.remove(digEffect);
    digEffect = null;
    blockEndurance = 0;
  }

  @Override
  protected void updateAction(PickaxeItem item, int delta) {
    blockEndurance -= delta * item.getStrength();
    digEffect.setCurrentEndurance(blockEndurance);
    soundController.playUnique(pickaxeSound);
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
    digRectangle(affectedRectangle);
  }

  public boolean isDigAllowed(Rectangle affectedRect) {
    Rectangle rect = new Rectangle(affectedRect.getX(), affectedRect.getY() - Sizes.BLOCK / 4,
        affectedRect.getWidth(), affectedRect.getHeight());
    Iterator<BackgroundElement> it = backgrounds.intersects (rect);
    while (it.hasNext()) {
      BackgroundElement el = it.next();
      if (el.requiresFoundation()) return false;
    }
    return true;
  }

  // This method should be used by other controllers as well.
  public boolean digRectangle(Rectangle affectedRect) {
    if (!isDigAllowed(affectedRect)) return false;
    Rectangle rect = new Rectangle(affectedRect.getX(), affectedRect.getY() - Sizes.BLOCK / 4,
        affectedRect.getWidth(), affectedRect.getHeight());
    blocks.getBlocks().setRectUnscaled(affectedRect, WallBlockType.EMPTY);
    blocks.getBackground().setRectUnscaled(affectedRect, BackgroundBlockType.DIRT);
    freeFallController.addObjectsInRectangle(rect);
    return true;
  }

  @Override
  public DroppableObject getObject(PickaxeItem item, int centerX, int centerY) {
    return null;
  }
}
