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

  public boolean add(T item) {
    assert Assert.validateSerializable(item);
    int newIndex = -1;
    for (int i = 0; i < getSize(); i++) {
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

  public boolean add(int index, T item) {
    assert Assert.validateSerializable(item);
    if (!items.get(index).isEmpty()) {
      return false;
    }

    items.set(index, item);
    if (items.get(selectedIndex).isEmpty()) {
      selectedIndex = index;
    }
    return true;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int x) {
    assert selectedIndex < getSize();
    selectedIndex = x;
  }

  public T getSelectedItem() {
    if (items.get(selectedIndex).isEmpty()) {
      return null;
    }
    return (T) items.get(selectedIndex);
  }

  public int getSize() {
    return SIZE;
  }

  @Override
  public List<T> getItems() {
    return new ArrayList<T>((Collection<? extends T>) items);
  }

  public void removeSelected() {
    items.set(selectedIndex, getEmptyItem());
  }

  public void remove(int index) {
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
