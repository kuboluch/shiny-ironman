package kniemkiewicz.jqblocks.ingame.inventory.item.renderer;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;

/**
 * User: knie
 * Date: 7/29/12
 */
public interface ItemRenderer<T extends Item> {

  void renderItem(T item, int x, int y, int square_size, boolean drawFlipped);

}
