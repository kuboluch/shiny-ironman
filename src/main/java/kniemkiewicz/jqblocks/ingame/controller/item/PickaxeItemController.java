package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class PickaxeItemController implements ItemController {

  @Autowired
  private SolidBlocks blocks;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  private Backgrounds backgrounds;

  @Override
  public void listen(List<InputEvent> events) {
    List<MousePressedEvent> mousePressedEvents = Collections3.collect(events, MousePressedEvent.class);

    if (!mousePressedEvents.isEmpty()) {
      handleMousePressedEvent(mousePressedEvents);
    }
  }

  public void handleMousePressedEvent(List<MousePressedEvent> mousePressedEvents) {
    assert mousePressedEvents.size() > 0;
    MousePressedEvent mpe = mousePressedEvents.get(0);
    int x = Sizes.roundToBlockSizeX(mpe.getLevelX());
    int y = Sizes.roundToBlockSizeY(mpe.getLevelY());
    Rectangle affectedBlock = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
    removeBlock(affectedBlock);
  }

  private void removeBlock(Rectangle rect) {
    Iterator<AbstractBlock> it = blocks.intersects(rect);
    if (it.hasNext()) {
      it.next().removeRect(rect, blocks, backgrounds);
    }
    assert !blocks.intersects(rect).hasNext();
  }
}
