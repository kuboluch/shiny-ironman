package kniemkiewicz.jqblocks.ingame.inventory.item.renderer;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 8/28/12
 */
public interface EquippedItemRenderer<T extends Item> {

  void renderEquippedItem(T item, Graphics g);

}
