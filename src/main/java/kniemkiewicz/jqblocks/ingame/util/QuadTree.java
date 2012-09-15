package kniemkiewicz.jqblocks.ingame.util;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.io.Serializable;
import java.util.*;

/**
 * User: knie
 * Date: 7/31/12
 *
 * I've checked that using intersects or intersectsUnique is slower than fullSearch, even for
 * taking the first element. It may not be the case only if there are many results. This
 * will be investigated further and intersect methods might get optimized more.
 */
public class QuadTree<T extends QuadTree.HasShape> {
  // Leafs containing more than this number of panelItems will get split into sub leafs. Note that
  // leaf may still have more than this number of panelItems if all them would span multiple sub leafs.
  static final int ITEMS_PER_LEAF = 5;

  int diffX;
  int diffY;
  int centerX;
  int centerY;

  public QuadTree(int width, int height, int centerX, int centerY) {
    // Assert width and height are power of 2.
    diffX = width / 4;
    diffY = height / 4;
    this.centerX = centerX;
    this.centerY = centerY;
  }

  public interface HasShape extends Serializable {
    Shape getShape();
  }

  // This generally a private struct.
  private static class Leaf<T extends HasShape> {
    List<T> objects = new ArrayList<T>();
    Leaf<T> topLeft = null;
    Leaf<T> topRight = null;
    Leaf<T> bottomLeft = null;
    Leaf<T> bottomRight = null;
    boolean hasSubLeafs = false;

    final void fillRectangles(List<Rectangle> rectangles, float cx, float cy, float dx, float dy) {
      rectangles.add(new Rectangle(cx - dx * 2, cy - dy * 2, 4 * dx, 4 * dy));
      if (topLeft != null) {
        topLeft.fillRectangles(rectangles, cx - dx, cy - dy, dx / 2, dy / 2);
      }
      if (topRight != null) {
        topRight.fillRectangles(rectangles, cx + dx, cy - dy, dx / 2, dy / 2);
      }
      if (bottomLeft != null) {
        bottomLeft.fillRectangles(rectangles, cx - dx, cy + dy, dx / 2, dy / 2);
      }
      if (bottomRight != null) {
        bottomRight.fillRectangles(rectangles, cx + dx, cy + dy, dx / 2, dy / 2);
      }
    }

    final void fillInterestingLeafs(float cx, float cy, float dx, float dy, Shape shape, List<Leaf<T>> results) {
      if (objects.size() > 0) {
        results.add(this);
      }
      if (!hasSubLeafs) return;
      float ddx = dx / 2;
      float ddy = dy / 2;
      if ((shape.getMinX() < cx) || (shape.getMinY() < cy)) {
        if (topLeft != null) {
          topLeft.fillInterestingLeafs(cx - dx, cy - dy, ddx, ddy, shape, results);
        }
      }
      if ((shape.getMaxX() > cx) || (shape.getMinY() < cy)) {
        if (topRight != null) {
          topRight.fillInterestingLeafs(cx + dx, cy - dy, ddx, ddy, shape, results);
        }
      }
      if ((shape.getMinX() < cx) || (shape.getMaxY() > cy)) {
        if (bottomLeft != null) {
          bottomLeft.fillInterestingLeafs(cx - dx, cy + dy, ddx, ddy, shape, results);
        }
      }
      if ((shape.getMaxX() > cx) || (shape.getMaxY() > cy)) {
        if (bottomRight != null) {
          bottomRight.fillInterestingLeafs(cx + dx, cy + dy, ddx, ddy, shape, results);
        }
      }
    }
  }

  Leaf<T> root = new Leaf<T>();
  Map<T, Leaf<T>> objectLeafMap = new HashMap<T, Leaf<T>>();

  final void splitLeaf(Leaf<T> leaf, float cx, float cy) {
    List<T> oldObjects = leaf.objects;
    leaf.objects = new ArrayList<T>();
    for (T ob : oldObjects) {
      Rectangle rect = GeometryUtils.getBoundingRectangle(ob.getShape());
      if ((rect.getMaxX() <= cx) && (rect.getMaxY() <= cy)) {
        if (leaf.topLeft == null) {
          leaf.topLeft = new Leaf<T>();
        }
        addToLeafExisting(ob, leaf.topLeft);
        continue;
      }
      if ((rect.getX() >= cx) && (rect.getMaxY() <= cy)) {
        if (leaf.topRight == null) {
          leaf.topRight = new Leaf<T>();
        }
        addToLeafExisting(ob, leaf.topRight);
        continue;
      }
      if ((rect.getMaxX() <= cx) && (rect.getY() >= cy)) {
        if (leaf.bottomLeft == null) {
          leaf.bottomLeft = new Leaf<T>();
        }
        addToLeafExisting(ob, leaf.bottomLeft);
        continue;
      }
      if ((rect.getX() >= cx) && (rect.getY() >= cy)) {
        if (leaf.bottomRight == null) {
          leaf.bottomRight = new Leaf<T>();
        }
        addToLeafExisting(ob, leaf.bottomRight);
        continue;
      }
      // It has to span subleafs.
      leaf.objects.add(ob);
    }
    leaf.hasSubLeafs = true;
  }

  public final void addToLeaf(T object, Leaf<T> leaf) {
    leaf.objects.add(object);
    assert !objectLeafMap.containsKey(object);
    objectLeafMap.put(object, leaf);
  }

  public final void addToLeafExisting(T object, Leaf<T> leaf) {
    leaf.objects.add(object);
    assert objectLeafMap.containsKey(object);
    objectLeafMap.put(object, leaf);
  }

  public final boolean add(T object) {
    Leaf<T> leaf = root;
    float cx = centerX;
    float cy = centerY;
    float dx = diffX;
    float dy = diffY;
    Rectangle rect = GeometryUtils.getBoundingRectangle(object.getShape());
    while (true) {
      if (leaf.objects.contains(object)) {
        return false;
      }
      if ((leaf.objects.size() + 1 > ITEMS_PER_LEAF) && !leaf.hasSubLeafs) {
        // We didn't split leaf yet, time to do so.
        splitLeaf(leaf, cx, cy);
      }
      boolean spansSubLeafs = ((rect.getX() < cx) && (cx < rect.getMaxX())) || ((rect.getY() < cy) && (cy < rect.getMaxY()));
      if (spansSubLeafs) {
        addToLeaf(object, leaf);
        return true;
      } else {
        // Leaf is still small.
        if (!leaf.hasSubLeafs) {
          addToLeaf(object, leaf);
          return true;
        }
        if ((rect.getMaxX() <= cx) && (rect.getMaxY() <= cy)) {
          if (leaf.topLeft == null) {
            leaf.topLeft = new Leaf<T>();
          }
          leaf = leaf.topLeft;
          cx -= dx;
          cy -= dy;
          dx /= 2;
          dy /= 2;
          continue;
        }
        if ((rect.getX() >= cx) && (rect.getMaxY() <= cy)) {
          if (leaf.topRight == null) {
            leaf.topRight = new Leaf<T>();
          }
          leaf = leaf.topRight;
          cx += dx;
          cy -= dy;
          dx /= 2;
          dy /= 2;
          continue;
        }
        if ((rect.getMaxX() <= cx) && (rect.getY() >= cy)) {
          if (leaf.bottomLeft == null) {
            leaf.bottomLeft = new Leaf<T>();
          }
          leaf = leaf.bottomLeft;
          cx -= dx;
          cy += dy;
          dx /= 2;
          dy /= 2;
          continue;
        }
        if ((rect.getX() >= cx) && (rect.getY() >= cy)) {
          if (leaf.bottomRight == null) {
            leaf.bottomRight = new Leaf<T>();
          }
          leaf = leaf.bottomRight;
          cx += dx;
          cy += dy;
          dx /= 2;
          dy /= 2;
          continue;
        }
        assert false;
      }
    }
  }

  public final List<T> fullSearch(Shape shape, List<T> objects) {
    List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(centerX, centerY, diffX, diffY, shape, leafs);
    for (Leaf<T> leaf : leafs) {
      for (T ob : leaf.objects) {
        if (GeometryUtils.intersects(ob.getShape(), shape)) {
          objects.add(ob);
        }
      }
    }
    return objects;
  }

  public final IterableIterator<T> intersects(final Shape shape) {
    final List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(centerX, centerY, diffX, diffY, shape, leafs);
    if (leafs.size() == 0) return Collections3.getIterable(Collections.<T>emptyList().iterator());
    return new IterableIterator<T>() {
      Iterator<Leaf<T>> leafIterator = leafs.iterator();
      Iterator<T> objectIterator = leafs.iterator().next().objects.iterator();
      T object = null;
      boolean finished = false;

      void update() {
        if (finished || (object != null)) return;
        while (true) {
          while (objectIterator.hasNext()) {
            object = objectIterator.next();
            if (GeometryUtils.intersects(object.getShape(), shape)) {
              return;
            }
          }
          if (leafIterator.hasNext()) {
            objectIterator = leafIterator.next().objects.iterator();
          } else {
            object = null;
            finished = true;
            return;
          }
        }
      }
      @Override
      public boolean hasNext() {
        update();
        return !finished;
      }

      @Override
      public T next() {
        update();
        T ob = object;
        object = null;
        return ob;
      }

      @Override
      public void remove() {
        assert false;
      }
    };
  }

  public final IterableIterator<T> intersectsUnique(final Shape shape) {
    final List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(centerX, centerY, diffX, diffY, shape, leafs);
    if (leafs.size() == 0) return Collections3.getIterable(Collections.<T>emptyList().iterator());
    final Set<T> returnedObjects = new HashSet<T>();
    return new IterableIterator<T>() {
      Iterator<Leaf<T>> leafIterator = leafs.iterator();
      Iterator<T> objectIterator = leafs.iterator().next().objects.iterator();
      T object = null;
      boolean finished = false;

      void update() {
        if (finished || (object != null)) return;
        while (true) {
          while (objectIterator.hasNext()) {
            object = objectIterator.next();
            if (!returnedObjects.contains(object) && GeometryUtils.intersects(object.getShape(), shape)) {
              return;
            }
          }
          if (leafIterator.hasNext()) {
            objectIterator = leafIterator.next().objects.iterator();
          } else {
            object = null;
            finished = true;
            return;
          }
        }
      }
      @Override
      public final boolean hasNext() {
        update();
        return !finished;
      }

      @Override
      public final T next() {
        update();
        T ob = object;
        returnedObjects.add(ob);
        object = null;
        return ob;
      }

      @Override
      public final void remove() {
        assert false;
      }
    };
  }


  public final boolean remove(T object) {
    if (!objectLeafMap.containsKey(object)) {
      return false;
    }
    assert objectLeafMap.get(object).objects.remove(object);
    objectLeafMap.remove(object);
    return true;
  }

  public final void listAll(List<T> objects) {
    objects.addAll(objectLeafMap.keySet());
  }

  public final boolean update(T object) {
    if (!remove(object)) {
      return false;
    }
    Assert.executeAndAssert(add(object));
    return true;
  }

  // This is only for debug.
  public final List<Rectangle> getRects() {
    List<Rectangle> rectangles = new ArrayList<Rectangle>();
    root.fillRectangles(rectangles, centerX, centerY, diffX, diffY);
    return rectangles;
  }


}
