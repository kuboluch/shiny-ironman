package kniemkiewicz.jqblocks.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * User: knie
 * Date: 7/22/12
 */
public class Assert {

  public static void executeAndAssert(boolean expression) {
    assert expression;
  }

  static private OutputStream noopStream = new OutputStream() {
    @Override
    public void write(int b) throws IOException { }
  };

  static private ObjectOutputStream ooStream;

  static {
    try{
      ooStream = new ObjectOutputStream(noopStream);
    } catch (IOException e) {
      // This should never happen.
      throw new RuntimeException("", e);
    }
  }

  // Returning boolean to make it easy to put into assert.
  // This method throws exception on failure.
  public static boolean validateSerializable(Serializable s) {
    try {
      ooStream.writeObject(s);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
