package kniemkiewicz.jqblocks.ingame.object.serialization;

import kniemkiewicz.jqblocks.util.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: knie
 * Date: 8/29/12
 */
public class TestSerializableTest {

  @Test
  public void testSimpleTreeEquals() throws Exception {
    TestSerializable t1 = new TestSerializable("t1");
    TestSerializable t1copy = new TestSerializable("t1");
    Assert.assertTrue(TestSerializable.treeEquals(t1, t1copy, new HashMap<TestSerializable, TestSerializable>()));
    t1copy = new TestSerializable("t1copy");
    Assert.assertTrue(!TestSerializable.treeEquals(t1, t1copy, new HashMap<TestSerializable, TestSerializable>()));
  }

  @Test
  public void testInfiniteLoop() throws Exception {
    TestSerializable t1 = new TestSerializable("t1");
    t1.add(t1);
    TestSerializable t1copy = new TestSerializable("t1");
    Assert.assertTrue(!TestSerializable.treeEquals(t1, t1copy, new HashMap<TestSerializable, TestSerializable>()));
    t1copy.add(t1copy);
    Assert.assertTrue(TestSerializable.treeEquals(t1, t1copy, new HashMap<TestSerializable, TestSerializable>()));
    t1copy.add(t1copy);
    Assert.assertTrue(!TestSerializable.treeEquals(t1, t1copy, new HashMap<TestSerializable, TestSerializable>()));
  }

  @Test
  public void testComplexCase() throws Exception {
    TestSerializable s2 = new TestSerializable("2");
    TestSerializable s3 = new TestSerializable("3");
    TestSerializable s4 = new TestSerializable("4");
    s2.add(s3);
    s3.add(s2);
    s4.add(s2);
    s4.add(s3);
    TestSerializable s2copy = new TestSerializable("2");
    TestSerializable s3copy = new TestSerializable("3");
    TestSerializable s4copy = new TestSerializable("4");
    s4copy.add(s2copy);
    s4copy.add(s3copy);
    Assert.assertTrue(!TestSerializable.treeEquals(s4, s4copy, new HashMap<TestSerializable, TestSerializable>()));
    s2copy.add(s3copy);
    Assert.assertTrue(!TestSerializable.treeEquals(s4, s4copy, new HashMap<TestSerializable, TestSerializable>()));
    s3copy.add(s2copy);
    Assert.assertTrue(TestSerializable.treeEquals(s4, s4copy, new HashMap<TestSerializable, TestSerializable>()));
    s3copy.add(s3copy);
    Assert.assertTrue(!TestSerializable.treeEquals(s4, s4copy, new HashMap<TestSerializable, TestSerializable>()));
  }
}
