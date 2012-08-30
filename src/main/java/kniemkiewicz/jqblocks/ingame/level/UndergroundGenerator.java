package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: knie
 * Date: 8/4/12
 */
@Component
public class UndergroundGenerator {

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Configuration configuration;

  void generateRock(final Random random) {

    float ROCK_PATCH_DENSITY = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_DENSITY", 0.015f);
    final float ROCK_PATCH_RADIUS = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_RADIUS", 1.5f);
    final float ROCK_PATCH_LENGTH = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_LENGTH", 5f);

    final int width = Sizes.LEVEL_SIZE_X / Sizes.BLOCK;
    final int height = Sizes.LEVEL_SIZE_Y / Sizes.BLOCK;
    final int count = (int) (ROCK_PATCH_DENSITY * width * height)/ 10;
    ExecutorService executor  = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    final AtomicInteger integer = new AtomicInteger();
    for (int i = 0; i < 20; i++) {
      executor.submit(new Runnable() {
        @Override
        public void run() {
          generateSingleThread(random.nextLong(), ROCK_PATCH_RADIUS, ROCK_PATCH_LENGTH, width, height, count, true);
          integer.incrementAndGet();
        }
      });
      executor.submit(new Runnable() {
        @Override
        public void run() {
          generateSingleThread(random.nextLong(), ROCK_PATCH_RADIUS, ROCK_PATCH_LENGTH, width, height, count, false);
          integer.incrementAndGet();
        }
      });
    }
    executor.shutdown();
    try {
      executor.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      assert false;
    }
  }

  private void generateSingleThread(long seed, float ROCK_PATCH_RADIUS, float ROCK_PATCH_LENGTH, int width, int height, int count, boolean toRock) {
    WallBlockType from;
    WallBlockType into;
    if (toRock) {
      from = WallBlockType.DIRT;
      into = WallBlockType.ROCK;
    } else {
      from = WallBlockType.ROCK;
      into = WallBlockType.DIRT;
    }
    Random random = new Random(seed);
    for (int i = 0; i < count; i++) {
      double r = random.nextGaussian() * 3 * ROCK_PATCH_RADIUS + ROCK_PATCH_RADIUS;
      int x0 = random.nextInt(width);
      int y0 = random.nextInt(height);
      int length = (int)Math.floor(random.nextGaussian() * 4 * ROCK_PATCH_LENGTH + ROCK_PATCH_LENGTH);
      for (int j = 0; j < length; j++) {
        if (r > 0) {
          for (int y = (int)Math.ceil(-r); y <= r; y++) {
            int dx = (int)Math.sqrt(r*r - y*y);
            for (int x = x0 - dx; x <= x0 + dx; x++) {
              if (solidBlocks.getBlocks().get(x, y + y0) == from) {
                solidBlocks.getBlocks().set(x, y + y0, into);
              }
            }
          }
        }
        y0 += 1;
        x0 += random.nextInt(3) - 1;
      }
    }
  }
}
