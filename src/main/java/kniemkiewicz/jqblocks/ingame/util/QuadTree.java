package kniemkiewicz.jqblocks.ingame.util;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

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
  // Leafs containing more than this number of objects will get split into sub leafs. Note that
  // leaf may still have more than this number of objects if all them would span multiple sub leafs.
  static final int ITEMS_PER_LEAF = 5;

  public interface HasShape {
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

    void fillRectangles(List<Rectangle> rectangles, float cx, float cy, float dx, float dy) {
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

    void fillInterestingLeafs(float cx, float cy, float dx, float dy, Shape shape, List<Leaf<T>> results) {
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

    public void listAll(List<T> objects) {
      objects.addAll(this.objects);
      if (hasSubLeafs) {
        if (topLeft != null) {
          topLeft.listAll(objects);
        }
        if (bottomLeft != null) {
          bottomLeft.listAll(objects);
        }
        if (topRight != null) {
          topRight.listAll(objects);
        }
        if (bottomRight != null) {
          bottomRight.listAll(objects);
        }
      }
    }
  }

  Leaf<T> root = new Leaf<T>();


  private static int CENTER_X = (Sizes.MAX_X + Sizes.MIN_X) / 2;
  private static int CENTER_Y = (Sizes.MAX_Y + Sizes.MIN_Y) / 2;
  private static int DIFF_X = (Sizes.MAX_X - Sizes.MIN_X) / 4;
  private static int DIFF_Y = (Sizes.MAX_Y - Sizes.MIN_Y) / 4;

  void splitLeaf(Leaf<T> leaf, float cx, float cy) {
    List<T> oldObjects = leaf.objects;
    leaf.objects = new ArrayList<T>();
    for (T ob : oldObjects) {
      Rectangle rect = GeometryUtils.getBoundingRectangle(ob.getShape());
      if ((rect.getMaxX() <= cx) && (rect.getMaxY() <= cy)) {
        if (leaf.topLeft == null) {
          leaf.topLeft = new Leaf<T>();
        }
        leaf.topLeft.objects.add(ob);
        continue;
      }
      if ((rect.getX() >= cx) && (rect.getMaxY() <= cy)) {
        if (leaf.topRight == null) {
          leaf.topRight = new Leaf<T>();
        }
        leaf.topRight.objects.add(ob);
        continue;
      }
      if ((rect.getMaxX() <= cx) && (rect.getY() >= cy)) {
        if (leaf.bottomLeft == null) {
          leaf.bottomLeft = new Leaf<T>();
        }
        leaf.bottomLeft.objects.add(ob);
        continue;
      }
      if ((rect.getX() >= cx) && (rect.getY() >= cy)) {
        if (leaf.bottomRight == null) {
          leaf.bottomRight = new Leaf<T>();
        }
        leaf.bottomRight.objects.add(ob);
        continue;
      }
      // It has to span subleafs.
      leaf.objects.add(ob);
    }
    leaf.hasSubLeafs = true;
  }

  public boolean add(T object) {
    Leaf<T> leaf = root;
    float cx = CENTER_X;
    float cy = CENTER_Y;
    float dx = DIFF_X;
    float dy = DIFF_Y;
    Rectangle rect = GeometryUtils.getBoundingRectangle(object.getShape());
    while (true) {
      if (leaf.objects.contains(object)) return false;
      if ((leaf.objects.size() + 1 > ITEMS_PER_LEAF) && !leaf.hasSubLeafs) {
        // We didn't split leaf yet, time to do so.
        splitLeaf(leaf, cx, cy);
      }
      boolean spansSubLeafs = ((rect.getX() < cx) && (cx < rect.getMaxX())) || ((rect.getY() < cy) && (cy < rect.getMaxY()));
      if (spansSubLeafs) {
        leaf.objects.add(object);
      } else {
        // Leaf is still small.
        if (!leaf.hasSubLeafs) {
          leaf.objects.add(object);
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

  public List<T> fullSearch(Shape shape, List<T> objects) {
    List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(CENTER_X, CENTER_Y, DIFF_X, DIFF_Y, shape, leafs);
    for (Leaf<T> leaf : leafs) {
      for (T ob : leaf.objects) {
        if (GeometryUtils.intersects(ob.getShape(), shape)) {
          objects.add(ob);
        }
      }
    }
    return objects;
  }

  public IterableIterator<T> intersects(final Shape shape) {
    final List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(CENTER_X, CENTER_Y, DIFF_X, DIFF_Y, shape, leafs);
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
        objectIterator.remove();
      }
    };
  }

  public IterableIterator<T> intersectsUnique(final Shape shape) {
    final List<Leaf<T>> leafs = new ArrayList<Leaf<T>>();
    root.fillInterestingLeafs(CENTER_X, CENTER_Y, DIFF_X, DIFF_Y, shape, leafs);
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
      public boolean hasNext() {
        update();
        return !finished;
      }

      @Override
      public T next() {
        update();
        T ob = object;
        returnedObjects.add(ob);
        object = null;
        return ob;
      }

      @Override
      public void remove() {
        objectIterator.remove();
      }
    };
  }


  public boolean remove(T object) {
    Leaf<T> leaf = root;
    float cx = CENTER_X;
    float cy = CENTER_Y;
    float dx = DIFF_X;
    float dy = DIFF_Y;
    Rectangle rect = GeometryUtils.getBoundingRectangle(object.getShape());
    while (true) {
      if (leaf.objects.remove(object)) return true;
      boolean spansSubLeafs = ((rect.getX() < cx) && (cx < rect.getMaxX())) || ((rect.getY() < cy) && (cy < rect.getMaxY()));
      if (!spansSubLeafs) {
        if ((rect.getMaxX() <= cx) && (rect.getMaxY() <= cy)) {
          if (leaf.topLeft == null) {
            return false;
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
            return false;
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
            return false;
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
            return false;
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

  public void listAll(List<T> objects) {
    root.listAll(objects);
  }

  // This is only for debug.
  public List<Rectangle> getRects() {
    List<Rectangle> rectangles = new ArrayList<Rectangle>();
    root.fillRectangles(rectangles, CENTER_X, CENTER_Y, DIFF_X, DIFF_Y);
    return rectangles;
  }


}
