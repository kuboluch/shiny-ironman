package kniemkiewicz.jqblocks.ingame.inventory;

import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Inventory<T> {

  public boolean add(T item);

  public boolean add(int index, T item);

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public T getSelectedItem();

  public int getSize();

  public void removeSelected();

  public void remove(int index);

  public List<T> getItems();

}