package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.CollisionController;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.block.levelWall.LevelWall;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Out;
import kniemkiewicz.jqblocks.util.TimeLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
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

  @Autowired
  CollisionController collisionController;

  @Autowired
  PointOfView pointOfView;

  Random random = new Random();

  public void generate() {
    TimeLog t = new TimeLog();
    Out<Integer> villageY = new Out<Integer>();
    int[] heights = surfaceGenerator.generate(random, villageY);
    t.logTimeAndRestart("surface generation");
    undergroundGenerator.generateRock(random);
    t.logTimeAndRestart("rock generation");
    villageGenerator.generateVillage(villageY.get());
    t.logTimeAndRestart("village generation");
    objectGenerator.generateTrees(random, heights);
    objectGenerator.generateBats(random, heights);
    objectGenerator.generateRocks(random, heights);
    t.logTimeAndRestart("object generation");
    generateLevelWalls();
  }

  final static public EnumSet<CollisionController.ObjectType> LEVEL_WALLS = EnumSet.of(CollisionController.ObjectType.LEVEL_WALLS);

  private void generateLevelWalls() {
    //TODO: this should use max possible screen size, not current.

    {
      // Left.
      float width = pointOfView.getWindowWidth() / 2 + Sizes.BLOCK * 2;
      Rectangle rect = new Rectangle(Sizes.MIN_X + pointOfView.getWindowWidth() / 2 + Sizes.BLOCK * 2,
          Sizes.MIN_Y - 100, width, Sizes.MAX_Y - Sizes.MIN_Y + 200);
      Assert.executeAndAssert(collisionController.add(LEVEL_WALLS, new LevelWall(rect), false));
    }
    {
      // Right.
      float width = pointOfView.getWindowWidth() / 2 + Sizes.BLOCK * 2;
      Rectangle rect = new Rectangle(Sizes.MAX_X - width,
          Sizes.MIN_Y - 100, width, Sizes.MAX_Y - Sizes.MIN_Y + 200);
      Assert.executeAndAssert(collisionController.add(LEVEL_WALLS, new LevelWall(rect), false));
    }
    {
      // Bottom.
      float width = pointOfView.getWindowHeight() / 2 + Sizes.BLOCK * 2;
      Rectangle rect = new Rectangle(Sizes.MIN_X - 100,
          Sizes.MAX_Y - width, Sizes.MAX_X - Sizes.MIN_X + 200, width);
      Assert.executeAndAssert(collisionController.add(LEVEL_WALLS, new LevelWall(rect), false));
    }
  }

  public void setSeed(long seed) {
    random.setSeed(seed);
  }
}
