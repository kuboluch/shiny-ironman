package kniemkiewicz.jqblocks.ingame.util;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import sun.net.www.protocol.gopher.GopherClient;

import java.util.ArrayList;
import java.util.List;

/**
 * User: knie
 * Date: 7/31/12
 */
public class QuadTree<T extends QuadTree.HasShape> {
  // Leafs containing more than this number of objects will get split into sub leafs. Note that
  // leaf may still have more than this number of objects if all them would span multiple sub leafs.
  static final int ITEMS_PER_LEAF = 5;

  public interface HasShape {
    Shape getShape();
  }
  private static class Leaf<T extends HasShape> {
    List<T> objects = new ArrayList<T>();
    Leaf<T> topLeft = null;
    Leaf<T> topRight = null;
    Leaf<T> bottomLeft = null;
    Leaf<T> bottomRight = null;
    boolean hasSubLeafs = false;

    public void fillRectangles(List<Rectangle> rectangles, float cx, float cy, float dx, float dy) {
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
  }

  private static class SearchIterator<T extends HasShape> extends IterableIterator<T> {

    float cx; // center of current Leaf.
    float cy; // center of current Leaf.
    float w; // half width of current Leaf.
    float h; // half height of current Leaf.

    @Override
    public boolean hasNext() {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public T next() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void remove() {
      //To change body of implemented methods use File | Settings | File Templates.
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

  public void add(T object) {
    Leaf<T> leaf = root;
    float cx = CENTER_X;
    float cy = CENTER_Y;
    float dx = DIFF_X;
    float dy = DIFF_Y;
    Rectangle rect = GeometryUtils.getBoundingRectangle(object.getShape());
    while (true) {
      if (leaf.objects.contains(object)) return;
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
          return;
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

  // This is only for debug, this is why it is based on recursion, can be slow.
  public List<Rectangle> getRects() {
    List<Rectangle> rectangles = new ArrayList<Rectangle>();
    root.fillRectangles(rectangles, CENTER_X, CENTER_Y, DIFF_X, DIFF_Y);
    return rectangles;
  }
}
