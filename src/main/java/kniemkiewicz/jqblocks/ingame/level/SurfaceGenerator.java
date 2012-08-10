package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RawEnumTable;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Out;
import kniemkiewicz.jqblocks.util.TimeLog;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private Configuration configuration;

  @Autowired
  private ObjectGenerator objectGenerator;

  @Autowired
  private SolidBlocks blocks;

  Random random;

  int[] heights = new int[(Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK];

  int[] generate(Random random, Out<Integer> villageY) {
    this.random = random;
    TimeLog t = new TimeLog();
    generateFlat();
    t.logTimeAndRestart("generate flat");
    generateHills();
    t.logTimeAndRestart("generate hills");
    runSlidingWindow(heights);
    flattenStartingPoint(villageY);
    t.logTimeAndRestart("sliding window");
    prepareBlocks(blocks);
    t.logTimeAndRestart("prepare blocks");
    return heights;
  }

  private void flattenStartingPoint(Out<Integer> startingPointY) {
    int startingPoint = (VillageGenerator.STARTING_X - Sizes.MIN_X) / Sizes.BLOCK;
    int minH = Integer.MAX_VALUE;
    for (int i = startingPoint - VillageGenerator.VILLAGE_RADIUS; i < startingPoint + VillageGenerator.VILLAGE_RADIUS; i++) {
      if (minH > heights[i]) {
        minH = heights[i];
      }
    }
    for (int i = startingPoint - VillageGenerator.VILLAGE_RADIUS; i < startingPoint + VillageGenerator.VILLAGE_RADIUS; i++) {
      heights[i] = minH;
    }
    startingPointY.set(Sizes.MAX_Y - minH);
  }

  // This method translates heights[x] into actual blocks, trying to use as few blocks as possible and making sure
  // that we use mostly horizontal blocks.
  private void prepareBlocks(SolidBlocks blocks) {
    RawEnumTable<WallBlockType> table = blocks.getBlocks();
    for (int i = 0; i < heights.length; i++) {
      table.setRectUnscaled(new Rectangle(Sizes.MIN_X + i * Sizes.BLOCK, Sizes.MAX_Y - heights[i], Sizes.BLOCK, heights[i]), WallBlockType.DIRT);
    }
  }

  private void generateFlat() {
    int y = Sizes.roundToBlockSize(4 * (Sizes.MAX_Y - Sizes.MIN_Y) / 5);
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

  private void generateHills() {
    float HILL_DENSITY = configuration.getFloat("SurfaceGenerator.HILL_DENSITY", 0.5f);
    int MAX_HILL_SIZE = configuration.getInt("SurfaceGenerator.MAX_HILL_SIZE", 60);
    int MIN_HILL_SIZE = configuration.getInt("SurfaceGenerator.MIN_HILL_SIZE", 20);
    int MIN_HILL_HEIGHT = configuration.getInt("SurfaceGenerator.MIN_HILL_HEIGHT", 4);
    int MAX_HILL_HEIGHT = configuration.getInt("SurfaceGenerator.MAX_HILL_HEIGHT", 16);
    int HILL_RND_HEIGHT = configuration.getInt("SurfaceGenerator.HILL_RND_HEIGHT", 3);
    int numberOfHills = (int)(2 * (Sizes.MAX_X - Sizes.MIN_X) / (MIN_HILL_SIZE + MAX_HILL_SIZE) * HILL_DENSITY);
    for (int i = 0; i < numberOfHills; i++) {
      int width = random.nextInt(MAX_HILL_SIZE - MIN_HILL_SIZE) + MIN_HILL_SIZE;
      int x = random.nextInt(heights.length - width - 1);
      int height = random.nextInt(MAX_HILL_HEIGHT - MIN_HILL_HEIGHT) + MIN_HILL_HEIGHT;
      assert width > 4;
      assert x + width < heights.length;
      int topPosition = random.nextInt(width - 4) + 2;
      for (int j = 0; j < topPosition; j++) {
        int rnd = HILL_RND_HEIGHT > 0 ? random.nextInt(HILL_RND_HEIGHT * 2) - HILL_RND_HEIGHT : 0;
        heights[x + j] += Sizes.BLOCK * (height * (j + 1) / topPosition + rnd);
      }
      int remainingWidth = width - topPosition;
      for (int j = topPosition; j < width; j++) {
        int rnd = HILL_RND_HEIGHT > 0 ? random.nextInt(HILL_RND_HEIGHT * 2) - HILL_RND_HEIGHT : 0;
        heights[x + j] += Sizes.BLOCK * ((height * (width - j) / remainingWidth + rnd));
      }
    }
  }

  void runSlidingWindow(int[] heightsArg) {
    int SLIDING_WINDOW_SIZE = configuration.getInt("SurfaceGenerator.SLIDING_WINDOW_SIZE", 1);
    int partialSums[] = new int[heightsArg.length];
    int sum = 0;
    for (int i = 0; i < heightsArg.length; i++) {
      sum += heightsArg[i];
      partialSums[i] = sum;
    }
    int length = heightsArg.length;
    for (int i = 0; i < SLIDING_WINDOW_SIZE; i++) {
      heightsArg[i] = Sizes.roundToBlockSize(partialSums[i + SLIDING_WINDOW_SIZE] / (i + 1 + SLIDING_WINDOW_SIZE));
      heightsArg[length - i - 1] = Sizes.roundToBlockSize((partialSums[length - 1] - partialSums[length - 2 - SLIDING_WINDOW_SIZE - i]) / (i + 1 + SLIDING_WINDOW_SIZE));
    }
    for (int i = SLIDING_WINDOW_SIZE; i < length - 1 - SLIDING_WINDOW_SIZE; i++) {
      heightsArg[i] = Sizes.roundToBlockSize((partialSums[i + SLIDING_WINDOW_SIZE] - partialSums[i - SLIDING_WINDOW_SIZE]) / (2 * SLIDING_WINDOW_SIZE));
    }
  }
}
