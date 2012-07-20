package kniemkiewicz.jqblocks.ingame.controller.item;

import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.DebugRenderableShape;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Color;
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

  public void listen(List<Click> clicks) {
    for (Click c : clicks) {
      int x = Sizes.roundToBlockSizeX(c.getX());
      int y = Sizes.roundToBlockSizeY(c.getY());
      Rectangle rect = new Rectangle(x, y, Sizes.BLOCK - 1 , Sizes.BLOCK - 1);
      Iterator<AbstractBlock> it = blocks.intersects(rect);
      if (it.hasNext()) {
        it.next().removeRect(rect, blocks);
      }
      Assert.assertThat(!blocks.intersects(rect).hasNext());
    }
  }
}
