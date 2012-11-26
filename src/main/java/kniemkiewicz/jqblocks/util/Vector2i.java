package kniemkiewicz.jqblocks.util;

/**
 * User: krzysiek
 * Date: 20.11.12
 * Pair<Integer,Integer> but faster.
 */
public class Vector2i {
  int x;
  int y;

  public Vector2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void swap() {
    int z = x;
    x = y;
    y = z;
  }

  public void add(Vector2i other) {
    x += other.x;
    y += other.y;
  }
}
