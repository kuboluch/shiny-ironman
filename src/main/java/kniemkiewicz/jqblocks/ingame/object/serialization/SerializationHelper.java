package kniemkiewicz.jqblocks.ingame.object.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * User: krzysiek
 * Date: 21.08.12
 */
public class SerializationHelper {
  private Map<Object, Integer> objectToIndex = new HashMap<Object, Integer>();
  private Queue<Object> objects = new LinkedList<Object>();
  private int lastIndex = 0;

  boolean addObject(Object object) {
    if (objectToIndex.containsKey(object)) return false;
    objectToIndex.put(object, lastIndex);
    objects.add(object);
    lastIndex++;
    return true;
  }

  int getSerializedId(Object object) {
    return objectToIndex.get(object);
  }

  private void writeObjects(ObjectOutputStream stream) throws IOException {
    while (!objects.isEmpty()) {
      Object ob = objects.poll();
      stream.writeObject(ob);
    }
  }

  static SerializationHelper HELPER;

  public static boolean add(Object object) {
    assert HELPER != null;
    return HELPER.addObject(object);
  }

  public static void startSerialization() {
    assert HELPER == null;
    HELPER = new SerializationHelper();
  }

  public static void flushData(ObjectOutputStream stream) throws IOException {
    assert HELPER != null;
    HELPER.writeObjects(stream);
    HELPER = null;
  }
}
