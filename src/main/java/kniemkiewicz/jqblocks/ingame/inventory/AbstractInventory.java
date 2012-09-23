package kniemkiewicz.jqblocks.ingame.inventory;

import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.inventory.ItemAddedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.inventory.ItemRemovedEvent;
import kniemkiewicz.jqblocks.ingame.controller.event.inventory.SelectedItemChangeEvent;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractInventory<T extends Item> implements Inventory<T> {

  public static final int SIZE = 10;

  @Autowired
  EventBus eventBus;

  protected List<Item> items = new ArrayList<Item>();

  protected int selectedIndex = 0;

  protected AbstractInventory() {
    for (int i = 0; i < getSize(); i++) {
      items.add(getEmptyItem());
    }
  }

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
    eventBus.broadcast(new ItemAddedEvent(this, item));
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
    eventBus.broadcast(new ItemAddedEvent(this, item));
    return true;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int x) {
    if (getSelectedIndex() != x) {
      eventBus.broadcast(new SelectedItemChangeEvent(this, getSelectedIndex(), x));
    }
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
    eventBus.broadcast(new ItemRemovedEvent(this, getSelectedItem()));
    items.set(selectedIndex, getEmptyItem());
  }

  public void remove(int index) {
    eventBus.broadcast(new ItemRemovedEvent(this, getItems().get(index)));
    items.set(index, getEmptyItem());
  }

  @Override
  public int indexOf(T item) {
    return items.indexOf(item);
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
