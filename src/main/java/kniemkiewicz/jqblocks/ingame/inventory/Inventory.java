package kniemkiewicz.jqblocks.ingame.inventory;

import java.util.List;

/**
 * User: qba
 * Date: 12.08.12
 */
public interface Inventory<T> {

  boolean add(T item);

  boolean add(int index, T item);

  int getSelectedIndex();

  void setSelectedIndex(int index);

  T getSelectedItem();

  int getSize();

  void removeSelected();

  void remove(int index);

  int indexOf(T item);

  List<T> getItems();

}