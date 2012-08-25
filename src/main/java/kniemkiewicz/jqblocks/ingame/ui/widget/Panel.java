package kniemkiewicz.jqblocks.ingame.ui.widget;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: qba
 * Date: 26.08.12
 */
public abstract class Panel<T extends Widget> extends ResizableFrame {

  static final int margin = 5;
  static final int emptyWidth = 0;
  static final int emptyHeight = 0;

  protected List<T> panelItems = new ArrayList<T>();

  public Panel() {
  }

  public void addItems(List<T> panelItems) {
    assert panelItems != null;
    for (T panelItem : panelItems) {
      addItem(panelItem);
    }
  }

  public void addItem(T panelItem) {
    assert panelItem != null;
    this.panelItems.add(panelItem);
    add(panelItem);
  }

  public void removeAllItems() {
    assert panelItems != null;
    Iterator<T> iter = panelItems.iterator();
    while (iter.hasNext()) {
      removeChild(iter.next());
      iter.remove();
    }
  }

  public void removeItem(T panelItem) {
    assert panelItem != null;
    this.panelItems.remove(panelItem);
    removeChild(panelItem);
  }

  @Override
  public int getPreferredInnerWidth() {
    if (panelItems.isEmpty()) {
      return getEmptyWidth();
    }
    return panelItems.get(0).getPreferredWidth();
  }

  @Override
  public int getPreferredInnerHeight() {
    if (panelItems.isEmpty()) {
      return getEmptyHeight();
    }
    int itemHeight = panelItems.get(0).getPreferredHeight();
    return itemHeight * panelItems.size() + getMargin() * (panelItems.size() - 1);
  }

  @Override
  protected void layout() {
    if (panelItems.isEmpty()) {
      return;
    }
    int itemHeight = panelItems.get(0).getPreferredHeight();

    for (int row = 0, y = getInnerY(); row < panelItems.size(); row++) {
      panelItems.get(row).adjustSize();
      panelItems.get(row).setPosition(getInnerX(), y);
      y += itemHeight + getMargin();
    }
  }

  public int getMargin() {
    return margin;
  }

  public int getEmptyWidth() {
    return emptyWidth;
  }

  public int getEmptyHeight() {
    return emptyHeight;
  }

  @Override
  protected boolean handleEvent(Event evt) {
    if (evt.isMouseEvent()) {
      final Event.Type eventType = evt.getType();

      // Needed for parent scrollPane to work
      if (eventType == Event.Type.MOUSE_WHEEL) {
        return false;
      }
    }

    if (super.handleEvent(evt)) {
      return true;
    }

    return false;
  }
}
