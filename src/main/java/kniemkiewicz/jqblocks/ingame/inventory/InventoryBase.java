package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.item.EmptyItem;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.util.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InventoryBase<T extends Item> implements Inventory<T> {
  public static final int SIZE = 10;

  protected List<Item> items = new ArrayList<Item>();

  protected int selectedIndex = 0;

  protected final Item emptyItem = new EmptyItem();

  protected int size = SIZE;

  public boolean add(T item) {
    assert Assert.validateSerializable(item);
    int newIndex = -1;
    for (int i = 0; i < size; i++) {
      if (items.get(i) == emptyItem) {
        newIndex = i;
        break;
      }
    }
    if (newIndex < 0) {
      return false;
    }
    items.set(newIndex, item);
    if (items.get(selectedIndex) == emptyItem) {
      selectedIndex = newIndex;
    }
    return true;
  }

  public void setSelectedIndex(int x) {
    assert selectedIndex < size;
    selectedIndex = x;
  }

  public T getSelectedItem() {
    if (items.get(selectedIndex) == emptyItem) {
      return null;
    }
    return (T) items.get(selectedIndex);
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void removeSelectedItem() {
    items.set(selectedIndex, emptyItem);
  }

  public void serializeItems(ObjectOutputStream stream) throws IOException {
    stream.writeObject(selectedIndex);
    for (Item item : items) {
      if (item == emptyItem) {
        stream.writeObject(null);
      } else {
        stream.writeObject(item);
      }
    }
  }

  public void loadSerializedItems(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    selectedIndex = (Integer)stream.readObject();
    for (int i = 0; i < getSize(); i++) {
      T item = (T) stream.readObject();
      if (item == null) {
        items.set(i, emptyItem);
      } else {
        items.set(i, item);
      }
    }
  }
}
