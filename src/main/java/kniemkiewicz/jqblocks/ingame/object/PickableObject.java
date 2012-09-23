package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.inventory.item.Item;

/**
 * User: knie
 * Date: 7/25/12
 */
public interface PickableObject extends PhysicalObject {

  Item getItem();

  PickableObjectType getType();

}
