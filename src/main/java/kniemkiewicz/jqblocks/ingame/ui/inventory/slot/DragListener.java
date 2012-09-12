package kniemkiewicz.jqblocks.ingame.ui.inventory.slot;

import de.matthiasmann.twl.Event;

/**
 * User: qba
 * Date: 10.09.12
 */
public interface DragListener<T> {

  public void dragStarted(DraggableSlot<T> slot, Event evt);

  public void dragging(DraggableSlot<T> slot, Event evt);

  public void dragStopped(DraggableSlot<T> slot, Event evt);

}
