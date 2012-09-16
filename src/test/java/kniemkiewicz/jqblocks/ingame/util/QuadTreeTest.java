package kniemkiewicz.jqblocks.ingame.util;

import junit.framework.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: krzysiek
 * Date: 16.09.12
 */
public class QuadTreeTest {

  class TestHasShape implements QuadTree.HasShape{
    final int id;
    final Shape shape;

    TestHasShape(int id, Shape shape) {
      this.id = id;
      this.shape = shape;
    }

    public int getId() {
      return id;
    }

    public Shape getShape() {
      return shape;
    }
  }

  void searchFor(QuadTree<TestHasShape> tree, Shape probe, int[] requiredIds) {
    List<TestHasShape> result = new ArrayList<TestHasShape>();
    tree.fullSearch(probe, result);
    Set<Integer> expected = new HashSet<Integer>();
    for (int i : requiredIds) {
      expected.add(i);
    }
    Set<Integer> returned = new HashSet<Integer>();
    for (TestHasShape t : result) {
      returned.add(t.getId());
    }
    Assert.assertEquals(expected, returned);
  }



  @Test
  public void testTopLeftCorner() {
    QuadTree<TestHasShape> tree = new QuadTree<TestHasShape>(4,4,2,2);
    searchFor(tree, new Rectangle(0, 0, 1, 1), new int[] {});
    tree.add(new TestHasShape(0, new Rectangle(0, 0, 4, 4)));
    searchFor(tree, new Rectangle(0, 0, 1, 1), new int[] {0});

    int[] ids = new int[QuadTree.ITEMS_PER_LEAF + 1];
    ids[0] = 0;
    for (int i = 1; i <= QuadTree.ITEMS_PER_LEAF; i++) {
      tree.add(new TestHasShape(-i, new Rectangle(2, 0, 1, 1)));
      ids[i] = -i;
    }
    searchFor(tree, new Rectangle(2, 0, 1, 1), ids);
    searchFor(tree, new Rectangle(0, 0, 1, 1), new int[] {0});
    tree.add(new TestHasShape(1, new Rectangle(2, 2, 2, 2)));
    searchFor(tree, new Rectangle(2, 2, 1, 1), new int[] {0, 1});
  }
}
