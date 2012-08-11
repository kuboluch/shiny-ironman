package kniemkiewicz.jqblocks.ingame.util;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Shape;

import java.util.Iterator;

public class LinearIntersectionIterator<T extends PhysicalObject> extends IterableIterator<T>{
  Iterator<T> it;
  Shape shape;
  T next = null;

  public LinearIntersectionIterator(Iterator<T> it, Shape shape) {
    this.it = it;
    this.shape = shape;
  }

  void updateNext() {
    if (next != null) return;
    while (it.hasNext()) {
      T b = it.next();
      if (GeometryUtils.intersects(b.getShape(), shape)) {
        next = b;
        return;
      }
    }
  }

  public boolean hasNext() {
    updateNext();
    return next != null;
  }

  public T next() {
    updateNext();
    T b = next;
    next = null;
    return b;
  }

  public void remove() {
    it.remove();
  }
}