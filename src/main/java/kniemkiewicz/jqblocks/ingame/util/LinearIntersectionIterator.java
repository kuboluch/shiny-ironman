package kniemkiewicz.jqblocks.ingame.util;

import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import org.newdawn.slick.geom.Shape;

import java.util.Iterator;

public class LinearIntersectionIterator<T extends PhysicalObject> implements Iterator<T> {
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
      if (b.getShape().intersects(shape)) {
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
    throw new UnsupportedOperationException();
  }
}