package kniemkiewicz.jqblocks.ingame.item;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * User: knie
 * Date: 7/29/12
 */
public interface ItemRenderer<T extends Item> {

  void renderItem(T item, Graphics g, int x, int y, int square_size, boolean drawFlipped);

  Image getImage(T item);
}
