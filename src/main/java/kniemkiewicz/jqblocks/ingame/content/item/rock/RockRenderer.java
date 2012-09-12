package kniemkiewicz.jqblocks.ingame.content.item.rock;

import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.GraphicsContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class RockRenderer implements ItemRenderer<RockItem> {

  @Autowired
  GraphicsContainer graphicsContainer;

  @Override
  public void renderItem(RockItem item, int x, int y, int square_size, boolean drawFlipped) {
    Graphics g = graphicsContainer.getGraphics();
    int radius = (int) (square_size * 0.4);
    int diff = square_size / 2 - radius;
    g.setColor(item.large ? Rock.LARGE_COLOR : Rock.SMALL_COLOR);
    g.fillOval(x + diff, y + diff, 2 * radius, 2 * radius);
    g.setColor(Color.black);
    g.drawOval(x + diff, y + diff, 2 * radius, 2 * radius);
  }
}
