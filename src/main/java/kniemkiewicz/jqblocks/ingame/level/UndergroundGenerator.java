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
    float ROCK_PATCH_DENSITY = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_DENSITY", 0.0008f);
    float ROCK_PATCH_RADIUS = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_RADIUS", 3f);
    float ROCK_PATCH_LENGTH = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_LENGTH", 5f);

    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int height = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    for (int i = 0; i < ROCK_PATCH_DENSITY * width * height; i++) {
      double r = random.nextGaussian() * 3 * ROCK_PATCH_RADIUS + ROCK_PATCH_RADIUS;
      int x0 = random.nextInt(width);
      int y0 = random.nextInt(height);
      int length = (int)Math.floor(random.nextGaussian() * 4 * ROCK_PATCH_LENGTH + ROCK_PATCH_LENGTH);
      for (int j = 0; j < length; j++) {
        if (r > 0) {
          for (int y = (int)Math.ceil(-r); y <= r; y++) {
            int dx = (int)Math.sqrt(r*r - y*y);
            for (int x = x0 - dx; x <= x0 + dx; x++) {
              if (solidBlocks.getBlocks().get(x, y + y0) == WallBlockType.DIRT) {
                solidBlocks.getBlocks().set(x, y + y0, WallBlockType.ROCK);
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
