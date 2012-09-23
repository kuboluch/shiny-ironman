package kniemkiewicz.jqblocks.ingame.inventory.item;

/**
 * User: qba
 * Date: 21.08.12
 */
public interface ItemFactory<T extends Item> {
  T createItem();
}
