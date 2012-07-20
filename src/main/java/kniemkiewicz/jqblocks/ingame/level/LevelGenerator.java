package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class LevelGenerator {

  @Autowired
  SolidBlocks blocks;

  @Autowired
  SurfaceGenerator surfaceGenerator;

  Random random = new Random();

  public void generate() {
    surfaceGenerator.generate(random, blocks);
  }

  public void setSeed(int seed) {
    random.setSeed(seed);
  }
}
