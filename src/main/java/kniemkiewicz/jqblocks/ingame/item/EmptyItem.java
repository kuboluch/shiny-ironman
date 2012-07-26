package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public class EmptyItem implements Item {

  public void renderItem(Graphics g, int x, int y, int square_size) { }

  public Class<? extends ItemController> getController() {
    return null;
  }

  @Override
  public boolean isLarge() {
    return false;
  }
}
