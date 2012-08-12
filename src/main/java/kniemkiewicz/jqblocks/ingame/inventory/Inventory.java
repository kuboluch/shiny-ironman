package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.item.Item;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Inventory<T> {

  public boolean add(T item);

  public void setSelectedIndex(int index);

  public T getSelectedItem();

  public int getSize();

  public void setSize(int size);

  public void removeSelectedItem();

}
