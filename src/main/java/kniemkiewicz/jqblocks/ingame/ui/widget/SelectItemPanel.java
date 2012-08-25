package kniemkiewicz.jqblocks.ingame.ui.widget;

import kniemkiewicz.jqblocks.ingame.ui.widget.model.PanelItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qba
 * Date: 20.08.12
 */
public class SelectItemPanel<T extends PanelItemModel> extends Panel<SelectablePanelItem<T>> {

  private static final int margin = 5;

  List<SelectListener<T>> listeners = new ArrayList<SelectListener<T>>();

  public SelectItemPanel(List<T> items) {
    super();
    for (T item : items) {
      addItem(new SelectablePanelItem<T>(item, item.getWidth(), item.getHeight()));
    }
  }

  public SelectItemPanel(List<T> items, int itemWidth, int itemHeight) {
    super();
    for (T item : items) {
      addItem(new SelectablePanelItem<T>(item, itemWidth, itemHeight));
    }
  }

  public void select(SelectablePanelItem<T> selectablePanelItem) {
    deselectAll();
    selectablePanelItem.select();
    notifyListeners(selectablePanelItem.getObject());
  }

  public void deselectAll() {
    for (SelectablePanelItem panelItem : panelItems) {
      panelItem.deselect();
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
