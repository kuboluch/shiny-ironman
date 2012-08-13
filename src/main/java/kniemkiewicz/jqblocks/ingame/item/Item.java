package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public interface Item extends Serializable {

  Class<? extends ItemController> getItemController();

  // renderItem is used only if getItemRenderer returns false.
  BeanName<? extends ItemRenderer> getItemRenderer();

  public void renderItem(Graphics g, int x, int y, int square_size);

  boolean isLarge();

  boolean isEmpty();
}
