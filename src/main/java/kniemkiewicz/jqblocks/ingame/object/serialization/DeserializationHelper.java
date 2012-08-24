package kniemkiewicz.jqblocks.ingame.object.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: krzysiek
 * Date: 21.08.12
 */
public class DeserializationHelper {
  private static List<SerializableRef> REFS = null;

  static void registerRef(SerializableRef ref) {
    REFS.add(ref);
  }

  static List<Object> deserializeObjects(ObjectInputStream stream) throws ClassNotFoundException, IOException {
    assert REFS == null;
    REFS = new ArrayList<SerializableRef>();
    List<Object> objects = (List<Object>) stream.readObject();
    assert objects instanceof ArrayList;
    // All refs should be already registered, during the deserialization. Time to point them to right objects.
    for (SerializableRef ref : REFS) {
      // Any exception here means we are missing something.
      ref.set(objects.get(ref.serializedId));
    }
    REFS = null;
    return objects;
  }
}
