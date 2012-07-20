package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.item.DirtBlockItemController;
import kniemkiewicz.jqblocks.ingame.object.DirtBlock;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public class DirtBlockItem implements Item {
  public void renderItem(Graphics g, int x, int y, int square_size) {
    DirtBlock.renderDirt(g, x, y, square_size, square_size);
  }

  public Class<? extends DirtBlockItemController> getController() {
    return DirtBlockItemController.class;
  }
}
