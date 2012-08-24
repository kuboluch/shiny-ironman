package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;

/**
 * User: qba
 * Date: 21.08.12
 */
public interface ItemFactory<T extends Item> {
  T createItem();
}
