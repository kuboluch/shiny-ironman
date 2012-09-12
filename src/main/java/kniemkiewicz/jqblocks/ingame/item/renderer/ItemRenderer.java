package kniemkiewicz.jqblocks.ingame.item.renderer;

import kniemkiewicz.jqblocks.ingame.item.Item;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * User: knie
 * Date: 7/29/12
 */
public interface ItemRenderer<T extends Item> {

  void renderItem(T item, int x, int y, int square_size, boolean drawFlipped);

}
