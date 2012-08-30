package kniemkiewicz.jqblocks.ingame.object.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class TestSerializable implements Serializable {
  static Map<String, AtomicInteger> serializationCounts = new HashMap<String, AtomicInteger>();

  private String name;
  private List<SerializableRef<TestSerializable>> refs = new ArrayList<SerializableRef<TestSerializable>>();

  TestSerializable(String name) {
    this.name = name;
  }

  void add(TestSerializable other) {
    refs.add(new SerializableRef<TestSerializable>(other));
  }

  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    //perform the default serialization for all non-transient, non-static fields
    outputStream.defaultWriteObject();
    if (!serializationCounts.containsKey(name)) {
      serializationCounts.put(name, new AtomicInteger());
    }
    serializationCounts.get(name).incrementAndGet();
  }

  // This method works fine only in this special case, not in general tree comparison problem.
  static boolean treeEquals(TestSerializable t1, TestSerializable t2, Map<TestSerializable, TestSerializable> suspectedEqual) {
    if (suspectedEqual.containsKey(t1)) {
      return suspectedEqual.get(t1) == t2;
    }
    // we suspect that t2 is equal to something and that cannot be t1 ...
    if (suspectedEqual.containsKey(t2)) return false;
    if (!t1.name.equals(t2.name)) return false;
    if (t1.refs.size() != t2.refs.size()) return false;
    suspectedEqual.put(t1, t2);
    suspectedEqual.put(t2, t1);
    for (int i = 0; i < t1.refs.size(); i++) {
      if (!treeEquals(t1.refs.get(i).get(), t2.refs.get(i).get(), suspectedEqual)) return false;
    }
    return true;
  }
}

