package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import kniemkiewicz.jqblocks.ingame.inventory.Inventory;

/**
 * User: qba
 * Date: 10.09.12
 */
public interface DraggableSlot<T> {

  boolean canDrop(T item);

  void resetDropState();

  boolean dropFrom(DraggableSlot<T> slot);

  T getModel();

  Inventory<T> getInventory();

  int getInventoryIndex();

}
