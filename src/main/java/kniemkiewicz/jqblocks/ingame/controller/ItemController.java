package kniemkiewicz.jqblocks.ingame.controller;

import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.item.Item;

import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public interface ItemController<T> {

  public void listen(T selectedItem, List<InputEvent> events);

}
