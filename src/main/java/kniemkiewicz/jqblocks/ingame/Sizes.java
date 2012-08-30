package kniemkiewicz.jqblocks.ingame;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Sizes {
  public static int BLOCK = 12;
  public static float TIME_UNIT = 150f; // Applied inside movement,
  public static float MAX_FALL_SPEED = Sizes.BLOCK * 20;
  public static float G = MAX_FALL_SPEED / 10;

  public static int DEFAULT_WINDOW_WIDTH = 1280;
  public static int DEFAULT_WINDOW_HEIGHT = 720;

  // This should be 2^x * BLOCK
  public static int MIN_X = -512  * BLOCK;
  public static int MAX_X = 512 * BLOCK;
  public static int MIN_Y = 0;
  public static int MAX_Y = 512 * BLOCK;
  public static int CENTER_X = (MAX_X + MIN_X) / 2;
  public static int CENTER_Y = (MAX_Y + MIN_Y) / 2;
  public static int LEVEL_SIZE_X = MAX_X - MIN_X;
  public static int LEVEL_SIZE_Y = MAX_Y - MIN_Y;

  public static int DEFAULT_BLOCK_ENDURANCE = 250;
  public static int DEFAULT_PICKAXE_STRENGTH = 1;
  public static int DEFAULT_AXE_STRENGTH = 1;
  public static int DEFAULT_RESOURCE_RICHNESS = 1;

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
