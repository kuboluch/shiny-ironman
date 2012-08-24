package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/21/12
 */
public class BowItem implements Item {

  public static Color ARROW_COLOR = new Color(100.0f/255, 50.0f/255, 0);

  @Override
  public void renderItem(Graphics g, int x, int y, int square_size) {
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

  @Override
  public Class<? extends ItemController> getItemController() {
    return BowItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return null;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
