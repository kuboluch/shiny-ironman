package kniemkiewicz.jqblocks.util;

import kniemkiewicz.jqblocks.ingame.object.serialization.SerializationHelper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * User: knie
 * Date: 7/22/12
 */
public class Assert {

  static public boolean ASSERT_ENABLED = false;

  private static boolean setAssertEnabled() {
    ASSERT_ENABLED = true;
    return true;
  }

  static {
    assert setAssertEnabled();
  }

  public static void executeAndAssert(boolean expression) {
    assert expression;
  }

  public static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError(condition);
    }
  }

  public static void assertTrue(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }

  static public OutputStream noopStream = new OutputStream() {
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
      SerializationHelper.startSerialization();
      ooStream.writeObject(s);
      SerializationHelper.flushData(ooStream);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
