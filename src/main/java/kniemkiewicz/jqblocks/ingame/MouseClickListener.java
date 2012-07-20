package kniemkiewicz.jqblocks.ingame;

import java.util.List;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public interface MouseClickListener {
  class Click {
    public Click(int x, int y, int clickCount) {
      this.x = x;
      this.y = y;
      this.clickCount = clickCount;
    }
    int x;
    int y;
    int clickCount;

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public int getClickCount() {
      return clickCount;
    }
  }
  void listen(List<Click> clicks);
}
