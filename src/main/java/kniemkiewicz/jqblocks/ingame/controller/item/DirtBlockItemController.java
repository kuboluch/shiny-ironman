package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.object.DirtBlock;
import kniemkiewicz.jqblocks.ingame.object.Player;
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

  public void listen(List<Click> clicks) {
    final Circle circle = new Circle(player.getXMovement().getPos() + Player.WIDTH / 2,
        player.getYMovement().getPos() + Player.HEIGHT / 2, RANGE);
    for (Click click : clicks) {
      if (circle.contains(click.getX(), click.getY())) {
        int blockX = Sizes.roundToBlockSizeX(click.getX());
        int blockY = Sizes.roundToBlockSizeY(click.getY());
        DirtBlock block = new DirtBlock(blockX, blockY, Sizes.BLOCK, Sizes.BLOCK);
        blocks.add(block);
      }
    }
  }
}
