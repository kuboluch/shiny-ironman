package kniemkiewicz.jqblocks.ingame.inventory;

import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Inventory<T> {

  public boolean add(T item);

  public void setSelectedIndex(int index);

  public T getSelectedItem();

  public int getSize();

  public void removeSelectedItem();

  public void removeItem(int index);

  public List<T> getItems();

}