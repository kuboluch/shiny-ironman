package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;

import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public interface ItemController<T extends Item> {

  public void listen(T selectedItem, List<Event> events);

  MovingPhysicalObject getDropObject(T item, int centerX, int centerY);
}
