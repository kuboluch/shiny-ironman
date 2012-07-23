package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MouseClickEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MousePressedEvent;
import kniemkiewicz.jqblocks.ingame.object.DirtBlock;
import kniemkiewicz.jqblocks.ingame.object.Player;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.geom.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
@Component
public class DirtBlockItemController implements ItemController {
  @Autowired
  Player player;

  @Autowired
  SolidBlocks blocks;

  public static final int RANGE = 75;

  @Override
  public void listen(List<InputEvent> events) {
    List<MouseClickEvent> clickEvents = Collections3.collect(events, MouseClickEvent.class);
    if (!clickEvents.isEmpty()) {
      handleClickEvent(clickEvents);
    }
  }

  public void handleClickEvent(List<MouseClickEvent> clicks) {
    final Circle circle = new Circle(player.getXMovement().getPos() + Player.WIDTH / 2,
        player.getYMovement().getPos() + Player.HEIGHT / 2, RANGE);
    for (MouseClickEvent click : clicks) {
      if (circle.contains(click.getLevelX(), click.getLevelY())) {
        int blockX = Sizes.roundToBlockSizeX(click.getLevelX());
        int blockY = Sizes.roundToBlockSizeY(click.getLevelY());
        DirtBlock block = new DirtBlock(blockX, blockY, Sizes.BLOCK, Sizes.BLOCK);
        blocks.add(block);
      }
    }
  }
}
