package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class BowRenderer implements ItemRenderer {

  public static Color ARROW_COLOR = new Color(100.0f/255, 50.0f/255, 0);

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size, boolean drawFlipped) {
    // TODO: Some numbers below work well for current square size and not for others.
    float x1 = x + square_size * 0.1f;
    float y1 = y + square_size * 0.1f;
    float x2 = x + square_size * 0.9f;
    float y2 = y + square_size * 0.9f;
    g.setLineWidth(2);
    g.setColor(Color.black);
    g.drawArc(x1 - 1, y1 - 1, square_size * 0.8f + 2, square_size * 0.8f + 2, 10, -45, 135);
    g.setColor(new Color(50, 50, 50));
    g.drawLine(x2, y1, x1, y2);
    g.setColor(ARROW_COLOR);
    g.drawLine(x1 + 5, y1 + 5, x2 + 1, y2 + 1);
    g.setLineWidth(1);
  }
}
