package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.util.Out;
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

  @Autowired
  VillageGenerator villageGenerator;

  @Autowired
  UndergroundGenerator undergroundGenerator;

  Random random = new Random();

  public void generate() {
    // TODO: This takes way too long on my laptop.
    TimeLog t = new TimeLog();
    Out<Integer> villageY = new Out<Integer>();
    int[] heights = surfaceGenerator.generate(random, villageY);
    t.logTimeAndRestart("surface generation");
    undergroundGenerator.generateRock(random);
    t.logTimeAndRestart("underground generation");
    villageGenerator.generateVillage(villageY.get());
    t.logTimeAndRestart("village generation");
    objectGenerator.generateTrees(random, heights);
    objectGenerator.generateBats(random, heights);
    objectGenerator.generateRocks(random, heights);
    t.logTimeAndRestart("object generation");
  }

  public void setSeed(long seed) {
    random.setSeed(seed);
  }
}
