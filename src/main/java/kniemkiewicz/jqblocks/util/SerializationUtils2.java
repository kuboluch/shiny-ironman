package kniemkiewicz.jqblocks.util;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;

import java.io.*;

/**
 * User: knie
 * Date: 7/30/12
 */
public final class SerializationUtils2 {

  // Note that this method can be used only inside serialization of other object, not directly
  // in serialization of Circle to OutputObjectStream.
  public static void serializeCircle(Circle circle, ObjectOutputStream stream) throws IOException {
    stream.writeFloat(circle.getCenterX());
    stream.writeFloat(circle.getCenterY());
    stream.writeFloat(circle.getRadius());
  }

  // Works only with serialization method above.
  public static Circle deserializeCircle(ObjectInputStream stream) throws IOException {
    float centerX = stream.readFloat();
    float centerY = stream.readFloat();
    float radius = stream.readFloat();
    return new Circle(centerX, centerY, radius);
  }

  // same restrictions as with Circle
  public static void serializeLine(Line line, ObjectOutputStream outputStream) throws IOException {
    outputStream.writeFloat(line.getX1());
    outputStream.writeFloat(line.getY1());
    outputStream.writeFloat(line.getX2());
    outputStream.writeFloat(line.getY2());
  }

  public static Line deserializeLine(ObjectInputStream inputStream) throws IOException {
    float x1 = inputStream.readFloat();
    float y1 = inputStream.readFloat();
    float x2 = inputStream.readFloat();
    float y2 = inputStream.readFloat();
    return new Line(x1, y1, x2, y2);
  }

  public static Pair<OutputStream, InputStream> getPipe() {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    InputStream inputStream = new InputStream() {
      byte[] data;
      int previousSize = -1;
      int currentPos = 0;
      @Override
      public int read() throws IOException {
        if (previousSize != outputStream.size()) {
          data = outputStream.toByteArray();
        }
        if (currentPos >= data.length) {
          return -1;
        }
        currentPos++;
        return data[currentPos - 1];
      }
    };
    return Pair.<OutputStream, InputStream>newInstance(outputStream, inputStream);
  }

  public static Pair<ObjectOutputStream, ObjectInputStream> getObjectPipe() {
    Pair<OutputStream, InputStream> bytePipe = getPipe();
    try {
      return Pair.newInstance(new ObjectOutputStream(bytePipe.getFirst()), new ObjectInputStream(bytePipe.getSecond()));
    } catch (IOException e) {
      assert false;
    }
    return null;
  }
}
