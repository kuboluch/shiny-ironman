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

  void generateRock(Random random) {
    float ROCK_PATCH_DENSITY = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_DENSITY", 0.015f);
    float ROCK_PATCH_RADIUS = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_RADIUS", 1.5f);
    float ROCK_PATCH_LENGTH = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_LENGTH", 5f);

    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int height = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    int count = (int) (ROCK_PATCH_DENSITY * width * height)/ 10;
    for (int i = 0; i < 10; i++) {
      generateSingleThread(random.nextLong(), ROCK_PATCH_RADIUS, ROCK_PATCH_LENGTH, width, height, count, true);

      generateSingleThread(random.nextLong(), ROCK_PATCH_RADIUS, ROCK_PATCH_LENGTH, width, height, count / 3, false);
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
