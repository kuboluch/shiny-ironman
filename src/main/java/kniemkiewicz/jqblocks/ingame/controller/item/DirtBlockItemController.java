package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseDraggedEvent;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.event.screen.ScreenMovedEvent;
import kniemkiewicz.jqblocks.ingame.input.MouseInput;
import kniemkiewicz.jqblocks.ingame.item.DirtBlockItem;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.block.DirtBlock;
import kniemkiewicz.jqblocks.ingame.object.player.Player;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public class DirtBlockItemController implements ItemController<DirtBlockItem> {
  public static final int RANGE = 75;

  @Autowired
  Player player;

  @Autowired
  SolidBlocks blocks;

  @Autowired
  MouseInput mouseInput;

  @Override
  public void listen(DirtBlockItem dirtBlock, List<Event> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);
    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(mousePressedEvents);
    }

    List<MouseDraggedEvent> mouseDraggedEvents = Collections3.collect(events, MouseDraggedEvent.class);
    if (!mouseDraggedEvents.isEmpty()) {
      handleMouseDraggedEvent(mouseDraggedEvents);
    }

    List<ScreenMovedEvent> screenMovedEvents = Collections3.collect(events, ScreenMovedEvent.class);
    if (!screenMovedEvents.isEmpty()) {
      handleScreenMovedEvent(screenMovedEvents);
    }
  }

  public void handleMousePressedEvent(List<MousePressedEvent> mousePressedEvents) {
    assert mousePressedEvents.size() > 0;
    MousePressedEvent mpe = mousePressedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mpe.getLevelX());
    int y = Sizes.roundToBlockSizeY(mpe.getLevelY());
    addBlock(x, y);
  }

  public void handleMouseDraggedEvent(List<MouseDraggedEvent> mouseDraggedEvents) {
    assert mouseDraggedEvents.size() > 0;
    MouseDraggedEvent mde = mouseDraggedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mde.getNewLevelX());
    int y = Sizes.roundToBlockSizeY(mde.getNewLevelY());
    addBlock(x, y);
  }

  public void handleScreenMovedEvent(List<ScreenMovedEvent> screenMovedEvents) {
    if (!mouseInput.isMouseButtonDown(0)) {
      return;
    }
    assert screenMovedEvents.size() > 0;
    ScreenMovedEvent sme = screenMovedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mouseInput.getMouseX() + sme.getNewShiftX());
    int y = Sizes.roundToBlockSizeY(mouseInput.getMouseY() + sme.getNewShiftY());
    addBlock(x, y);
  }

  private void addBlock(int x, int y) {
    if (isInRange(x, y)) {
      Rectangle blockRect = new Rectangle(x, y, Sizes.BLOCK - 1, Sizes.BLOCK - 1);
      AbstractBlock block = getBlock(blockRect);
      if (block == null) {
        DirtBlock newBlock = new DirtBlock(x, y, Sizes.BLOCK, Sizes.BLOCK);
        blocks.add(newBlock);
      }
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

  public boolean isInRange(int x, int y) {
    final Circle circle = new Circle(player.getXMovement().getPos() + Player.WIDTH / 2,
        player.getYMovement().getPos() + Player.HEIGHT / 2, RANGE);
    return circle.contains(x, y);
  }
}
