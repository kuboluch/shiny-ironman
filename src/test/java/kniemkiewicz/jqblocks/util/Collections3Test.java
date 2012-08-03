package kniemkiewicz.jqblocks.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: knie
 * Date: 7/28/12
 */
public class Collections3Test {
  @Test
  public void iterateOverAllTestEmpty() {
    List<List<Integer>> lists = new ArrayList<List<Integer>>();
    IterableIterator<Integer> it = Collections3.iterateOverAll(lists.iterator());
    Assert.assertTrue(!it.hasNext());
  }
  @Test
  public void iterateOverAllTest() {
    List<List<Integer>> lists = new ArrayList<List<Integer>>();
    lists.add(Arrays.<Integer>asList(1, 2));
    lists.add(Arrays.<Integer>asList());
    lists.add(Arrays.<Integer>asList(3));
    lists.add(Arrays.<Integer>asList(4, 5));
    IterableIterator<Integer> it = Collections3.iterateOverAll(lists.iterator());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(it.next(), (Integer)1);
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(it.next(), (Integer)2);
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(it.next(), (Integer)3);
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(it.next(), (Integer)4);
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(it.next(), (Integer)5);
    Assert.assertFalse(it.hasNext());
  }
  @Test
  public void iterateOverAllTestNoHasNext() {
    List<List<Integer>> lists = new ArrayList<List<Integer>>();
    lists.add(Arrays.<Integer>asList(1, 2));
    lists.add(Arrays.<Integer>asList());
    lists.add(Arrays.<Integer>asList(3));
    lists.add(Arrays.<Integer>asList(4, 5));
    IterableIterator<Integer> it = Collections3.iterateOverAll(lists.iterator());
    Assert.assertEquals(it.next(), (Integer)1);
    Assert.assertEquals(it.next(), (Integer)2);
    Assert.assertEquals(it.next(), (Integer)3);
    Assert.assertEquals(it.next(), (Integer)4);
    Assert.assertEquals(it.next(), (Integer)5);
    Assert.assertFalse(it.hasNext());
  }
}
