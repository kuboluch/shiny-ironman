package kniemkiewicz.jqblocks.ingame.object.serialization;

import kniemkiewicz.jqblocks.util.SerializationUtils2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: krzysiek
 * Date: 21.08.12
 */
public class SerializableRef<T> {
  transient T object = null;
  Integer serializedId = null;

  public SerializableRef(T object) {
    this.object = object;
  }

  public SerializableRef() { }

  public T get() {
    assert serializedId == null;
    return object;
  }

  public void set(T object) {
    this.serializedId = null;
    this.object = object;
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    // This will add this object if it wasn't listed already.
    Object ob = get();
    if (ob != null) {
      SerializationHelper.add(ob);
      this.serializedId = SerializationHelper.HELPER.getSerializedId(ob);
    }
    //perform the default serialization for all non-transient, non-static fields.
    outputStream.defaultWriteObject();
    this.serializedId = null;
  }


  // need to implement serialization as Circle is not Serializable
  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    DeserializationHelper.registerRef(this);
  }
}
