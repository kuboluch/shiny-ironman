package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.util.TimeLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class LevelGenerator {

  public static Log logger = LogFactory.getLog(LevelGenerator.class);

  @Autowired
  SurfaceGenerator surfaceGenerator;

  @Autowired
  ObjectGenerator objectGenerator;

  Random random = new Random();

  public void generate() {
    // TODO: This takes way too long on my laptop.
    TimeLog t = new TimeLog();
    int[] heights = surfaceGenerator.generate(random);
    t.logTimeAndRestart("level generation");
    objectGenerator.generateTrees(random, heights);
    objectGenerator.generateBats(random, heights);
    t.logTimeAndRestart("generate objects");
  }

  public void setSeed(int seed) {
    random.setSeed(seed);
  }
}
