package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.DirtBlock;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class SurfaceGenerator {

  Random random;
  int[] heights = new int[(Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK];

  void generate(Random random, SolidBlocks blocks) {
    this.random = random;
    generateFlat();

    prepareBlocks(blocks);
  }

  // This method translates heights[x] into actual blocks, trying to use as few blocks as possible and making sure
  // that we use mostly horizontal blocks.
  private void prepareBlocks(SolidBlocks blocks) {
    //TODO: Use something else than Rectangle, it is too expensive here.
    //TODO: Consider using Stack
    List<Rectangle> proposals = new ArrayList<Rectangle>();
    int h = heights[0];
    // width is zero as it is unknown yet.
    proposals.add(new Rectangle(Sizes.MIN_X, Sizes.MAX_Y - heights[0], 0, heights[0]));
    for (int i = 1; i < heights.length; i++) {
      if (heights[i] > h) {
        // we have to add some more blocks, above the existing proposals.
        proposals.add(new Rectangle(Sizes.MIN_X + i * Sizes.BLOCK, Sizes.MAX_Y - heights[i], 0, heights[i] - h));
      }
      if (heights[i] < h) {
        // Some blocks may end completely.
        int new_y = Sizes.MAX_Y - heights[i];
        while(new_y >= proposals.get(proposals.size() - 1).getMaxY()) {
          Rectangle r = proposals.get(proposals.size() - 1);
          proposals.remove(proposals.size() - 1);
          assert blocks.add(new DirtBlock(r.getMinX(), r.getMinY(), Sizes.MIN_X + i * Sizes.BLOCK - r.getMinX(), r.getHeight()));
        }
        // We should never reach bottom of the level so there is always at least the last block that we can cut into
        // smaller one if new height is lowest ever seen.
        if (new_y > proposals.get(proposals.size() - 1).getMinY()) {
          Rectangle r = proposals.get(proposals.size() - 1);
          int diff = (int)(new_y - r.getMinY());
          assert blocks.add(new DirtBlock(r.getMinX(), r.getMinY(), Sizes.MIN_X + i * Sizes.BLOCK - r.getMinX(), diff));
          r.setY(r.getY() + diff);
          r.setHeight(r.getHeight() - diff);
        }
      }
      h = heights[i];
    }
    for (Rectangle r : proposals) {
      assert blocks.add(new DirtBlock(r.getMinX(), r.getMinY(), Sizes.MAX_X - r.getMinX(), r.getHeight()));
    }
  }

  private void generateFlat() {
    int y = 4 * (Sizes.MAX_Y - Sizes.MIN_Y) / 5;
    int i = 0;
    while (i < heights.length) {
      int dx = random.nextInt(10);
      int dy = random.nextInt(3) - 1;
      y += dy * Sizes.BLOCK;
      for (int x = i; x < i + dx && x < heights.length; x++) {
        heights[x] = y;
      }
      i += dx;
    }
  }
}
