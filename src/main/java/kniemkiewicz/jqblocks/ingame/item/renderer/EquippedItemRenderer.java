package kniemkiewicz.jqblocks.ingame.item.renderer;

import kniemkiewicz.jqblocks.ingame.item.Item;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 8/28/12
 */
public interface EquippedItemRenderer<T extends Item> {

  void renderEquippedItem(T item, Graphics g);

}
