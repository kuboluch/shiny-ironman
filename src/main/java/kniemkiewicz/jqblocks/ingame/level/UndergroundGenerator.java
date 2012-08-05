package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.block.DirtBlock;
import kniemkiewicz.jqblocks.ingame.object.block.RockBlock;
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
    float ROCK_PATCH_DENSITY = configuration.getFloat("UndergroundGenerator.ROCK_PATCH_DENSITY", 0.001f);

    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int height = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    BlockShape blockShape = new BlockShape();
    for (int i = 0; i < ROCK_PATCH_DENSITY * width * height; i++) {
      int r = (int)random.nextGaussian() * 10 + 10;
      if (r > 0) {
        blockShape.addCircle(random.nextInt(width), random.nextInt(height), r);
      }
    }
    Map<Integer, TreeSet<BlockShape.Interval>> intervals = blockShape.getIntervals();
    for (Integer y : intervals.keySet()) {
      for (BlockShape.Interval interval : intervals.get(y)) {
        int x = interval.begin * Sizes.BLOCK + Sizes.MIN_X;
        int yy = y * Sizes.BLOCK + Sizes.MIN_Y;
        int rectWidth = (interval.end - interval.begin) * Sizes.BLOCK;
        Rectangle rect = new Rectangle(x, yy, rectWidth, Sizes.BLOCK);
        for (AbstractBlock block : solidBlocks.intersects(rect)) {
          if (block instanceof DirtBlock) {
            Rectangle removedRect = block.removeRect(rect, solidBlocks);
            if (removedRect != null) {
              solidBlocks.add(new RockBlock(removedRect.getX(), removedRect.getY(), removedRect.getWidth(), removedRect.getHeight()));
            }
          }
        }
      }
    }
  }
}
