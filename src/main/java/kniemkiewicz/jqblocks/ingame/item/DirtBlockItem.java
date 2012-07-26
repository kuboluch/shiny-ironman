package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.item.DirtBlockItemController;
import kniemkiewicz.jqblocks.ingame.object.block.DirtBlock;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public class DirtBlockItem implements Item {
  public void renderItem(Graphics g, int x, int y, int square_size) {
    x+= square_size / 10;
    y+= square_size / 10;
    square_size = square_size * 9 / 10;
    g.setColor(DirtBlock.BROWN);
    g.fillRoundRect(x, y, square_size, square_size, 5);
    g.setColor(DirtBlock.DARK_GREEN);
    g.drawRoundRect(x, y, square_size, square_size, 5);
    g.drawRoundRect(x +1, y + 1, square_size - 2, square_size - 2, 5);
  }

  public Class<? extends DirtBlockItemController> getController() {
    return DirtBlockItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }
}
