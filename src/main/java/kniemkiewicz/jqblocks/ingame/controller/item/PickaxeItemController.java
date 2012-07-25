package kniemkiewicz.jqblocks.ingame.controller.item;

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
public class PickaxeItemController implements UpdateQueue.UpdateController<PickaxeItem>, ItemController<PickaxeItem> {
  public static Log logger = LogFactory.getLog(PickaxeItemController.class);

  public static final int RANGE = 75;

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  Player player;

  @Autowired
  private RenderQueue renderQueue;

  @Autowired
  MouseInput mouseInput;

  @Autowired
  Backgrounds backgrounds;

  private Rectangle affectedBlock;

  private DigEffect digEffect;

  private int blockEndurance = 0;

  private void startDigging(PickaxeItem pickaxe, Rectangle rect, AbstractBlock block) {
    affectedBlock = rect;
    blockEndurance = block.getEndurance();
    updateQueue.add(pickaxe);
    digEffect = new DigEffect(blockEndurance, rect);
    renderQueue.add(digEffect);
  }

  private void stopDigging(PickaxeItem pickaxe) {
    renderQueue.remove(digEffect);
    digEffect = null;
    affectedBlock = null;
    blockEndurance = 0;
    updateQueue.remove(pickaxe);
  }

  @Override
  public void listen(PickaxeItem pickaxe, List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(pickaxe, mousePressedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      handleMouseDraggedEvent(pickaxe, mouseDraggedEvents);
    }

    List<MouseReleasedEvent> mouseReleasedEvents = Collections3.collect(events, MouseReleasedEvent.class);
    if (!mouseReleasedEvents.isEmpty()) {
      handleMouseReleasedEvent(pickaxe, mouseReleasedEvents);
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      handleScreenMovedEvent(pickaxe, screenMovedEvents);
    }
  }

  public void handleMousePressedEvent(PickaxeItem pickaxe, List<MousePressedEvent> mousePressedEvents) {
    assert mousePressedEvents.size() > 0;
    MousePressedEvent mpe = mousePressedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mpe.getLevelX());
    int y = Sizes.roundToBlockSizeY(mpe.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }
    Rectangle blockRect = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
    AbstractBlock block = getBlock(blockRect);
    if (block != null) {
      logger.debug("startDigging on pressed [blockRect="+blockRect+"]");
      startDigging(pickaxe, blockRect, block);
    }
  }

  public void handleMouseDraggedEvent(PickaxeItem pickaxe, List<MouseDraggedEvent> mouseDraggedEvents) {
    assert mouseDraggedEvents.size() > 0;
    MouseDraggedEvent mde = mouseDraggedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    handleMouseCoordChange(pickaxe, x, y);
  }

  public void handleScreenMovedEvent(PickaxeItem pickaxe, List<ScreenMovedEvent> screenMovedEvents) {
    if (!mouseInput.isMouseButtonDown(0)) {
      return;
    }
    assert screenMovedEvents.size() > 0;
    ScreenMovedEvent sme = screenMovedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mouseInput.getMouseX() + sme.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(mouseInput.getMouseY() + sme.getNewShiftY());
    handleMouseCoordChange(pickaxe, x, y);
  }

  private void handleMouseCoordChange(PickaxeItem pickaxe, int x, int y) {
    Rectangle blockRect = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
    if (affectedBlock != null && (!affectedBlock.intersects(blockRect)) || !isInRange(x, y)) {
      logger.debug("stopDigging on dragged [affectedBlock="+affectedBlock+"]");
      stopDigging(pickaxe);
    }
    if (!isInRange(x, y)) {
      return;
    }
    AbstractBlock block = getBlock(blockRect);
    if (affectedBlock == null && block != null) {
      logger.debug("startDigging on dragged [blockRect="+blockRect+"]");
      startDigging(pickaxe, blockRect, block);
    }
  }

  public void handleMouseReleasedEvent(PickaxeItem pickaxe, List<MouseReleasedEvent> mouseReleasedEvents) {
    if (affectedBlock == null) {
      return;
    }
    assert mouseReleasedEvents.size() > 0;
    MouseReleasedEvent mre = mouseReleasedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mre.getLevelX());
    int y = Sizes.roundToBlockSizeY(mre.getLevelY());
    if (!isInRange(x, y)) {
      return;
    }
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
    stopDigging(pickaxe);
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

  public boolean isInRange(int x, int y) {
    final Circle circle = new Circle(player.getXMovement().getPos() + Player.WIDTH / 2,
        player.getYMovement().getPos() + Player.HEIGHT / 2, RANGE);
    return circle.contains(x, y);
  }

  @Override
  public void update(PickaxeItem pickaxe, int delta) {
    if (affectedBlock == null) {
      return;
    }
    logger.debug("update [affectedBlock="+affectedBlock+", enduranceLeft="+blockEndurance+"]");
    blockEndurance -= delta * pickaxe.getStrength();
    digEffect.setCurrentEndurance(blockEndurance);
    if (blockEndurance <= 0) {
      AbstractBlock block = getBlock(affectedBlock);
      if (block != null) {
        logger.debug("removeBlock on update [affectedBlock="+affectedBlock+"]");
        removeBlock(block);
      }
      logger.debug("stopDigging on update [affectedBlock="+affectedBlock+"]");
      stopDigging(pickaxe);
    }
  }
}
