package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;

/**
 * User: krzysiek
 * Date: 19.11.12
 */
public final class BresenhamsLine {

  public static <T extends Enum<T> & RenderableBlockType> void drawLine(int x0, int y0, int x1, int y1, RawEnumTable<T> table, T value) {
    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);
    boolean steep = dy > dx;
    int buf;
    if (steep) {
      buf = x0; x0 = y0; y0 = buf; // Swap x0 y0
      buf = x1; x1 = y1; y1 = buf; // Swap x1 y1
    }
    if (x0 > x1) {
      buf = x0; x0 = x1; x1 = buf; // Swap x0 x1
      buf = y0; y0 = y1; y1 = buf; // Swap x0 x1
    }
    int err = (dx / 2), ystep = (y0 < y1 ? 1 : -1), y = y0;
    if (y0 == y1) {
      ystep = 0;
    }

    for (int x = x0; x <= x1; ++x) {
      if (steep) {
        table.set(y, x, value);
      } else {
        table.set(x, y, value);
      }
      err = err - dy;
      if (err < 0) {
        y += ystep;  err += dx;
      }
    }
  }
}
