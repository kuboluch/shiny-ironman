package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MouseReleasedEvent;
import kniemkiewicz.jqblocks.ingame.item.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.util.Collections3;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class PickaxeItemController implements UpdateQueue.UpdateController<PickaxeItem>, ItemController {
  public static Log logger = LogFactory.getLog(PickaxeItemController.class);

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  private Backgrounds backgrounds;

  @Autowired
  PickaxeItem pickaxe;

  private Rectangle affectedBlock;

  private int blockEndurance = 0;

  private void startDigging(Rectangle rect, AbstractBlock block) {
    affectedBlock = rect;
    blockEndurance = block.getEndurance();
    updateQueue.add(pickaxe);
  }

  private void stopDigging() {
    affectedBlock = null;
    blockEndurance = 0;
    updateQueue.remove(pickaxe);
  }

  @Override
  public void listen(List<InputEvent> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(mousePressedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      handleMouseDraggedEvent(mouseDraggedEvents);
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.collect(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      handleMouseReleasedEvent(mouseReleasedEvents);
    }
  }

  public void handleMousePressedEvent(List<MousePressedEvent> mousePressedEvents) {
    assert mousePressedEvents.size() > 0;
    MousePressedEvent mpe = mousePressedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mpe.getLevelX());
    int y = Sizes.roundToBlockSizeY(mpe.getLevelY());
    Rectangle blockRect = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
    AbstractBlock block = getBlock(blockRect);
    if (block != null) {
      logger.debug("startDigging on pressed [blockRect="+blockRect+"]");
      startDigging(blockRect, block);
    }
  }

  public void handleMouseDraggedEvent(List<MouseDraggedEvent> mouseDraggedEvents) {
    assert mouseDraggedEvents.size() > 0;
    MouseDraggedEvent mde = mouseDraggedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    Rectangle blockRect = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
    if (affectedBlock != null && !affectedBlock.intersects(blockRect)) {
      logger.debug("stopDigging on dragged [affectedBlock="+affectedBlock+"]");
      stopDigging();
    }
    AbstractBlock block = getBlock(blockRect);
    if (affectedBlock == null && block != null) {
      logger.debug("startDigging on dragged [blockRect="+blockRect+"]");
      startDigging(blockRect, block);
    }
  }

  public void handleMouseReleasedEvent(List<MouseReleasedEvent> mouseReleasedEvents) {
    if (affectedBlock == null) {
      return;
    }
    assert mouseReleasedEvents.size() > 0;
    MouseReleasedEvent mre = mouseReleasedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mre.getLevelX());
    int y = Sizes.roundToBlockSizeY(mre.getLevelY());
    if (blockEndurance <= 0) {
      if (affectedBlock.intersects(new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1))) {
        AbstractBlock block = getBlock(affectedBlock);
        if (block != null) {
          logger.debug("removeBlock on release [affectedBlock="+affectedBlock+"]");
          removeBlock(block);
        }
      }
    }
    logger.debug("stopDigging on release [affectedBlock="+affectedBlock+"]");
    stopDigging();
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
    block.removeRect(affectedBlock, blocks, backgrounds);
    assert !blocks.intersects(affectedBlock).hasNext();
  }

  @Override
  public void update(PickaxeItem pickaxe, int delta) {
    if (affectedBlock == null) {
      return;
    }
    logger.debug("update [affectedBlock="+affectedBlock+", enduranceLeft="+blockEndurance+"]");
    blockEndurance -= delta * pickaxe.getStrength();
    if (blockEndurance <= 0) {
      AbstractBlock block = getBlock(affectedBlock);
      if (block != null) {
        logger.debug("removeBlock on update [affectedBlock="+affectedBlock+"]");
        removeBlock(block);
      }
      logger.debug("stopDigging on update [affectedBlock="+affectedBlock+"]");
      stopDigging();
    }
  }
}
