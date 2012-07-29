package kniemkiewicz.jqblocks.ingame.object.rock;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/25/12
 */
public class RockItem implements Item {

  boolean large;

  public RockItem(boolean large) {
    this.large = large;
  }

  @Override
  public void renderItem(Graphics g, int x, int y, int square_size) {
    int radius = (int) (square_size * 0.4);
    int diff = square_size / 2 - radius;
    g.setColor(large ? Rock.LARGE_COLOR : Rock.SMALL_COLOR);
    g.fillOval(x + diff, y + diff, 2 * radius, 2 * radius);
    g.setColor(Color.black);
    g.drawOval(x + diff, y + diff, 2 * radius, 2 * radius);
  }

  @Override
  public Class<? extends ItemController> getItemController() {
    return RockItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return null;
  }

  @Override
  public boolean isLarge() {
    return large;
  }
}
