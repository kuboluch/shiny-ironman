package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public interface Item {
  void renderItem(Graphics g, int x, int y, int square_size);

  Class<? extends ItemController> getController();

  boolean isLarge();
}
