package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import com.google.common.base.Optional;
import kniemkiewicz.jqblocks.ingame.inventory.Inventory;

/**
 * User: qba
 * Date: 10.09.12
 */
public interface DraggableSlot<T> {

  boolean canDrop();

  void resetDropState();

  boolean dropFrom(DraggableSlot<T> slot);

  T getModel();

  Inventory<T> getInventory();

  int getInventoryIndex();

}
