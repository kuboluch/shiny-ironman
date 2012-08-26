package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class DirtBlockItemRenderer implements ItemRenderer<DirtBlockItem> {
  @Override
  public void renderItem(DirtBlockItem item, Graphics g, int x, int y, int square_size, boolean drawFlipped) {
    x+= square_size / 10;
    y+= square_size / 10;
    square_size = square_size * 9 / 10;
    g.setColor(DirtBlockTypeRenderer.BROWN);
    g.fillRoundRect(x, y, square_size, square_size, 5);
    g.setColor(DirtBlockTypeRenderer.DARK_GREEN);
    g.drawRoundRect(x, y, square_size, square_size, 5);
    g.drawRoundRect(x +1, y + 1, square_size - 2, square_size - 2, 5);
  }
}
