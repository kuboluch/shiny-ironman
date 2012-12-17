package kniemkiewicz.jqblocks.ingame.level;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.BackgroundBlockType;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.Vector2i;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: krzysiek
 * Date: 20.11.12
 */
@Component
public class RandomCaveGenerator {

  public static Log logger = LogFactory.getLog(RandomCaveGenerator.class);

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Configuration configuration;

  float SIZE_TO_POINTS_RATIO = 0;

  @PostConstruct
  void init() {
    SIZE_TO_POINTS_RATIO = configuration.getFloat("RandomCaveGenerator.SIZE_TO_POINTS_RATIO", 2);
  }

  static final Function<Vector2i, Integer> GET_X_FUN = new Function<Vector2i, Integer>() {
    @Override
    public Integer apply(Vector2i input) {
      return input.getX();
    }
  };

  // Points will be ordered by x
  ArrayList<Vector2i> getOrderedPoints(int dx, int dy, int maxPointsPerWall, Random random) {
    int points = (maxPointsPerWall > 0 ? random.nextInt(maxPointsPerWall) : 0) + 1;
    ArrayList<Vector2i> li = new ArrayList<Vector2i>();
    Set<Integer> used = new HashSet<Integer>(points);
    for (int i = 0; i < points; i++) {
      int x = random.nextInt(dx + 1);
      if (used.contains(x)) continue;
      used.add(x);
      li.add(new Vector2i(x, random.nextInt(dy + 1)));
    }
    Collections3.sortByFunction(li, GET_X_FUN);
    return li;
  }

  private final void set(int x, int y) {
    solidBlocks.getBlocks().set(x, y, WallBlockType.EMPTY);
    solidBlocks.getBackground().set(x, y, BackgroundBlockType.DIRT);
  }

  void makeCave(float cx, float cy, float dx, float dy, Random random) {
    assert SIZE_TO_POINTS_RATIO >= 1;
    RawEnumTable<WallBlockType> table = solidBlocks.getBlocks();
    int width = (int) (dx / Sizes.BLOCK);
    int height = (int) (dy / Sizes.BLOCK);
    int dd = Math.min(width, height) / 3;
    int x0 = table.toXIndex((int) (cx - dx / 2)), y0 = table.toYIndex((int) (cy - dy / 2));
    generateContour(random, width, height, dd, x0, y0);
    int x1 = x0 + dd,         y1 = y0 + dd;
    int x2 = x0 + width - dd, y2 = y0 + height - dd;
    int x3 = x0 + width, y3 = y0 + height;
    // left
    for (int y = y0; y <= y3; y++) {
      boolean inside = false;
      for (int x = x0; x <= x1; x++) {
        if (table.get(x, y) == WallBlockType.EMPTY) {
          inside = true;
        } else {
          if (inside) {
           set(x, y);
          }
        }
      }
    }
    // right
    for (int y = y0; y <= y3; y++) {
      boolean inside = false;
      for (int x = x3; x >= x2; x--) {
        if (table.get(x, y) == WallBlockType.EMPTY) {
          inside = true;
        } else {
          if (inside) {
            set(x, y);
          }
        }
      }
    }
    // top
    for (int x = x1; x <= x2; x++) {
      boolean inside = false;
      for (int y = y0; y <= y1; y++) {
        if (table.get(x, y) == WallBlockType.EMPTY) {
          inside = true;
        } else {
          if (inside) {
            set(x, y);
          }
        }
      }
    }
    // bottom
    for (int x = x1; x <= x2; x++) {
      boolean inside = false;
      for (int y = y3; y >= y2; y--) {
        if (table.get(x, y) == WallBlockType.EMPTY) {
          inside = true;
        } else {
          if (inside) {
            set(x, y);
          }
        }
      }
    }
    // center
    for (int x = x1; x <= x2; x++) {
      boolean inside = false;
      for (int y = y1; y < y2; y++) {
        if (table.get(x, y) == WallBlockType.EMPTY) {
          if (!inside) {
            inside = true;
          } else {
            break;
          }
          continue;
        }
        if (inside) {
          set(x, y);
        }
      }
    }
  }

  private void generateContour(Random random, int width, int height, int dd, int x0, int y0) {
    int maxPointsPerWallX = (int)((width - 2 * dd) / SIZE_TO_POINTS_RATIO );
    int maxPointsPerWallY = (int)((height - 2 * dd) / SIZE_TO_POINTS_RATIO );
    int x1 = x0 + dd,         y1 = y0 + dd;
    int x2 = x0 + width - dd, y2 = y0 + height - dd;
    List<List<Vector2i>> allPoints = new ArrayList<List<Vector2i>>();
    {
      // Top rectangle.
      Vector2i corner = new Vector2i(x1, y0);
      List<Vector2i> points = getOrderedPoints(width - 2 * dd, dd, maxPointsPerWallX, random);
      for (Vector2i v : points) {
        v.add(corner);
      }
      allPoints.add(points);
    }
    {
      // Right rectangle.
      Vector2i corner = new Vector2i(x2, y1);
      List<Vector2i> points = getOrderedPoints(height - 2 * dd, dd, maxPointsPerWallY, random);
      for (Vector2i v : points) {
        v.swap();
        v.add(corner);
      }
      allPoints.add(points);
    }
    {
      // Bottom rectangle.
      Vector2i corner = new Vector2i(x1, y2);
      ArrayList<Vector2i> points = getOrderedPoints(width - 2 * dd, dd, maxPointsPerWallX, random);
      for (Vector2i v : points) {
        v.add(corner);
      }
      allPoints.add(Lists.reverse(points));
    }
    {
      // Left rectangle.
      Vector2i corner = new Vector2i(x0, y1);
      List<Vector2i> points = getOrderedPoints(height - 2 * dd, dd, maxPointsPerWallY, random);
      for (Vector2i v : points) {
        v.swap();
        v.add(corner);
      }
      allPoints.add(Lists.reverse(points));
    }
    Vector2i prev = null;
    for (Vector2i v : Collections3.iterateOverAll(allPoints.iterator())) {
      if (prev != null) {
        BresenhamsLine.drawLine(v.getX(), v.getY(), prev.getX(), prev.getY(), solidBlocks.getBlocks(), WallBlockType.EMPTY);
        BresenhamsLine.drawLine(v.getX(), v.getY(), prev.getX(), prev.getY(), solidBlocks.getBackground(), BackgroundBlockType.DIRT);
      }
      prev = v;
    }
    Vector2i first = allPoints.get(0).get(0);
    BresenhamsLine.drawLine(first.getX(), first.getY(), prev.getX(), prev.getY(), solidBlocks.getBlocks(), WallBlockType.EMPTY);
    BresenhamsLine.drawLine(first.getX(), first.getY(), prev.getX(), prev.getY(), solidBlocks.getBackground(), BackgroundBlockType.DIRT);
  }
}
