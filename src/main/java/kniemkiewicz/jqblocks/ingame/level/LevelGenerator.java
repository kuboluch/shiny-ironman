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
  SolidBlocks blocks;

  @Autowired
  SurfaceGenerator surfaceGenerator;

  Random random = new Random();

  public void generate() {
    // TODO: This takes way too long on my laptop.
    TimeLog t = new TimeLog();
    surfaceGenerator.generate(random, blocks);
    t.logTimeAndRestart("level generation");
  }

  public void setSeed(int seed) {
    random.setSeed(seed);
  }
}
