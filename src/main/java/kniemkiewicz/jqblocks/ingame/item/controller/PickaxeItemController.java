package kniemkiewicz.jqblocks.ingame.item.controller;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.MouseInput;
import kniemkiewicz.jqblocks.ingame.item.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundFactory;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.util.Collections3;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

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
  boolean canPerformAction(Rectangle rect) {
    return getBlock(rect) != null;
  }

  @Override
  void startAction(PickaxeItem item) {
    AbstractBlock block = getAffectedBlock();
    blockEndurance = block.getEndurance();
    digEffect = new DigEffect(blockEndurance, affectedBlock);
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
    return getBlock(affectedBlock);
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
    block.removeRect(affectedBlock, blocks);
    backgrounds.add(backgroundFactory.getNaturalDirtBackground(
        affectedBlock.getX(), affectedBlock.getY(), affectedBlock.getWidth(), affectedBlock.getHeight()));
    assert !blocks.intersects(affectedBlock).hasNext();
  }

  @Override
  public MovingPhysicalObject getDropObject(PickaxeItem item, int centerX, int centerY) {
    return null;
  }
}
