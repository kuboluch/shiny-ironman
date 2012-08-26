package kniemkiewicz.jqblocks.ingame.item;

import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/29/12
 */
public interface ItemRenderer<T extends Item> {

  void renderItem(T item, Graphics g, int x, int y, int square_size, boolean drawFlipped);
}
