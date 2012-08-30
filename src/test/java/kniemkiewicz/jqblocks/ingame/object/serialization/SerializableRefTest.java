package kniemkiewicz.jqblocks.ingame.object.serialization;

import junit.framework.Assert;
import kniemkiewicz.jqblocks.util.Pair;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: krzysiek
 * Date: 22.08.12
 */
public class SerializableRefTest {

  Pair<ObjectOutputStream, ObjectInputStream> streams = SerializationUtils2.getObjectPipe();

  List<TestSerializable> objects = new ArrayList<TestSerializable>();

  private void checkAsserts() throws IOException, ClassNotFoundException {
    SerializationHelper.startSerialization();
    for (TestSerializable s : objects) {
      Assert.assertTrue(SerializationHelper.add(s));
    }
    SerializationHelper.flushData(streams.getFirst());
    List<TestSerializable> copy = (List<TestSerializable>) DeserializationHelper.deserializeObjects(streams.getSecond());
    Assert.assertEquals(objects.size(), copy.size());
    Map<TestSerializable, TestSerializable> suspectedEqual = new HashMap<TestSerializable, TestSerializable>();
    for (int i = 0; i < copy.size(); i++) {
      Assert.assertTrue(TestSerializable.treeEquals(objects.get(i), copy.get(i), suspectedEqual));
    }
  }

  @Test
  public void testSimpleSerialization() throws Exception {
    objects.add(new TestSerializable("1"));
    objects.add(new TestSerializable("2"));
    objects.add(new TestSerializable("3"));
    objects.add(new TestSerializable("4"));
    checkAsserts();
  }

  @Test
  public void testSerializeReferences() throws Exception {
    TestSerializable s1 = new TestSerializable("1");
    objects.add(s1);
    objects.add(new TestSerializable("2"));
    s1.add(objects.get(1));
    objects.add(new TestSerializable("3"));
    s1.add(objects.get(2));
    checkAsserts();
  }

  @Test
  public void testSerializeDeduplicate() throws Exception {
    TestSerializable s1 = new TestSerializable("1");
    objects.add(s1);
    s1.add(s1);
    TestSerializable s2 = new TestSerializable("2");
    TestSerializable s3 = new TestSerializable("3");
    TestSerializable s4 = new TestSerializable("4");
    objects.add(s2);
    objects.add(s3);
    objects.add(s4);
    s2.add(s3);
    s3.add(s2);
    s4.add(s2);
    s4.add(s3);
    checkAsserts();
  }
  @Test
  public void testSerializeHidden() throws Exception {
    TestSerializable s2 = new TestSerializable("2");
    TestSerializable s3 = new TestSerializable("3");
    TestSerializable s4 = new TestSerializable("4");
    objects.add(s2);
    objects.add(s4);
    s2.add(s3);
    s3.add(s2);
    s4.add(s2);
    s4.add(s3);
    checkAsserts();
  }
}
