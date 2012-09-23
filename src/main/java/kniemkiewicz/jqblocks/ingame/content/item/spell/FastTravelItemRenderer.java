package kniemkiewicz.jqblocks.ingame.content.item.spell;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.GraphicsContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
@Component
public class FastTravelItemRenderer implements ItemRenderer {

  @Autowired
  GraphicsContainer graphicsContainer;

  @Override
  public void renderItem(Item item, int x, int y, int square_size, boolean drawFlipped) {
    // This is very poor method to do this, direct use of opengl would be orders of magnitude faster.
    for (int i = square_size / 2; i >=0; i--) {
      Graphics g = graphicsContainer.getGraphics();
      float c = 1 - 1f * i / square_size * 2;
      assert c <= 1;
      g.setColor(new Color(c, c, 1));
      g.fillOval(x + square_size / 2 - i, y + square_size / 2 - i, 2 * i, 2 * i);
    }
  }
}
