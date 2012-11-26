package kniemkiewicz.jqblocks.util;

import java.io.*;

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

    String last = "";

    @Override
    public void write(int b) throws IOException {
      /* This was useful for debugging crappy TWL messages.
      StringWriter sw = new StringWriter();
      new Throwable("").printStackTrace(new PrintWriter(sw));
      String stackTrace = sw.toString();
      if (stackTrace.equals(last)) return;
      last = stackTrace;
      System.out.println(last);*/
    }
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
