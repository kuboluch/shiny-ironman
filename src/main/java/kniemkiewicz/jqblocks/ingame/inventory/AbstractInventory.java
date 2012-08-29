package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.util.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractInventory<T extends Item> implements Inventory<T> {

  public static final int SIZE = 10;

  protected List<Item> items = new ArrayList<Item>();

  protected int selectedIndex = 0;

  protected int size = SIZE;

  public boolean add(T item) {
    assert Assert.validateSerializable(item);
    int newIndex = -1;
    for (int i = 0; i < size; i++) {
      if (items.get(i).isEmpty()) {
        newIndex = i;
        break;
      }
    }
    if (newIndex < 0) {
      return false;
    }
    items.set(newIndex, item);
    if (items.get(selectedIndex).isEmpty()) {
      selectedIndex = newIndex;
    }
    return true;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int x) {
    assert selectedIndex < size;
    selectedIndex = x;
  }

  public T getSelectedItem() {
    if (items.get(selectedIndex).isEmpty()) {
      return null;
    }
    return (T) items.get(selectedIndex);
  }

  public int getSize() {
    return size;
  }

  @Override
  public List<T> getItems() {
    return new ArrayList<T>((Collection<? extends T>) items);
  }

  public void removeSelectedItem() {
    items.set(selectedIndex, getEmptyItem());
  }

  public void removeItem(int index) {
    items.set(index, getEmptyItem());
  }

  abstract protected T getEmptyItem();

  public void serializeItems(ObjectOutputStream stream) throws IOException {
    stream.writeObject(selectedIndex);
    for (Item item : items) {
      if (item.isEmpty()) {
        stream.writeObject(getEmptyItem());
      } else {
        stream.writeObject(item);
      }
    }
  }

  public void loadSerializedItems(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    selectedIndex = (Integer)stream.readObject();
    for (int i = 0; i < getSize(); i++) {
      T item = (T) stream.readObject();
      if (item.isEmpty()) {
        items.set(i, getEmptyItem());
      } else {
        items.set(i, item);
      }
    }
  }
}
