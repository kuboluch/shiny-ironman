package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.controller.event.Event;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;

import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public interface ItemController<T extends Item> {

  void listen(T selectedItem, List<Event> events);

  DroppableObject getObject(T item, int centerX, int centerY);

}
