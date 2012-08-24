package kniemkiewicz.jqblocks.ingame.ui.widget;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class SelectObjectPanel<T extends Selectable> extends ResizableFrame {

  private static final int margin = 5;

  protected List<SelectableObject<T>> selectableObjects = new ArrayList<SelectableObject<T>>();

  List<SelectListener<T>> listeners = new ArrayList<SelectListener<T>>();

  public SelectObjectPanel(List<T> objects) {
    assert objects != null;
    for (T object : objects) {
      SelectableObject<T> selectableObject = new SelectableObject<T>(object);
      selectableObjects.add(selectableObject);
      add(selectableObject);
    }
  }

  @Override
  public int getPreferredInnerWidth() {
    return selectableObjects.get(0).getPreferredWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    int itemHeight = selectableObjects.get(0).getPreferredHeight();
    return itemHeight * selectableObjects.size() + getMargin() * (selectableObjects.size() - 1);
  }

  @Override
  protected void layout() {
    int itemHeight = selectableObjects.get(0).getPreferredHeight();

    for (int row = 0, y = getInnerY(); row < selectableObjects.size(); row++) {
      selectableObjects.get(row).adjustSize();
      selectableObjects.get(row).setPosition(getInnerX(), y);
      y += itemHeight + getMargin();
    }
  }

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEvent()) {
      final Event.Type eventType = evt.getType();

      if (eventType == Event.Type.MOUSE_WHEEL) {
        return false;
      }
    }

    if (super.handleEvent(evt)) {
      return true;
    }

    return false;
  }

  public void select(SelectableObject<T> selectableObject) {
    deselectAll();
    selectableObject.select();
    notifyListeners(selectableObject.getObject());
  }

  public void deselectAll() {
    for (SelectableObject object : selectableObjects) {
      object.deselect();
    }
  }

  public int getMargin() {
    return margin;
  }

  void notifyListeners(T object) {
    for (SelectListener<T> listener : listeners) {
      listener.onSelect(object);
    }
  }

  public void addSelectListener(SelectListener<T> listener) {
    listeners.add(listener);
  }

  public void removeSelectListener(SelectListener<T> listener) {
    listeners.remove(listener);
  }
}
