package kniemkiewicz.jqblocks.ingame;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Sizes {
  public static int BLOCK = 20;
  public static float TIME_UNIT = 150f;
  public static float MAX_FALL_SPEED = Sizes.BLOCK / TIME_UNIT * 10;
  public static float G = MAX_FALL_SPEED / TIME_UNIT / 10;

  public static int DEFAULT_WINDOW_WIDTH = 1000;
  public static int DEFAULT_WINDOW_HEIGHT = 800;

  public static int MIN_X = -10 * DEFAULT_WINDOW_WIDTH;
  public static int MAX_X = 10 * DEFAULT_WINDOW_WIDTH;
  public static int MIN_Y = 0;
  public static int MAX_Y = 10 * DEFAULT_WINDOW_HEIGHT;

  static public int roundToBlockSizeX(int x) {
    return (x - MIN_X) / BLOCK * BLOCK + MIN_X;
  }

  static public int roundToBlockSizeX(float x) {
    return Math.round((x - MIN_X) / BLOCK) * BLOCK + MIN_X;
  }

  static public int roundToBlockSizeY(int y) {
    return (y - MIN_Y) / BLOCK * BLOCK + MIN_Y;
  }

  static public int roundToBlockSizeY(float y) {
    return Math.round((y - MIN_Y) / BLOCK) * BLOCK + MIN_Y;
  }

  // For widths and heights
  static public int roundToBlockSize(int z) {
    return z / BLOCK * BLOCK;
  }

  static public int roundToBlockSize(float z) {
    return Math.round(z / BLOCK) * BLOCK;
  }
}
