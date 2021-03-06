package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.DroppableObject;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.content.background.Tree;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.item.rock.Rock;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * User: knie
 * Date: 7/23/12
 */
@Component
public class ObjectGenerator {

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  Configuration configuration;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  VillageGenerator villageGenerator;

  void generateTrees(Random random, int[] heights) {
    int center = (VillageGenerator.STARTING_X - Sizes.MIN_X) / Sizes.BLOCK;
    int radius = VillageGenerator.VILLAGE_RADIUS + 2;
    generateTrees(random, heights, 0, center - radius);
    generateTrees(random, heights, center + radius, heights.length);
  }

  void generateTrees(Random random, int[] heights, int from, int until) {
    float TREE_DENSITY = configuration.getFloat("ObjectGenerator.TREE_DENSITY", 0.7f);
    int REQUIRED_OK_BLOCKS = Tree.WIDTH / Sizes.BLOCK - 1;
    int prevHeight = heights[from];
    int okBlocks = 0;

    for (int i = from; i < until; i++) {
      if (heights[i] == prevHeight) {
        okBlocks++;
        if ((okBlocks == REQUIRED_OK_BLOCKS) && (random.nextFloat() < TREE_DENSITY)) {
          okBlocks = -1; // Let the trees have at least one block between them.
          int x = Sizes.MIN_X + (i + 1) * Sizes.BLOCK - Tree.WIDTH;
          int y = Sizes.MAX_Y -  heights[i] - Tree.HEIGHT;
          backgrounds.add(new Tree(x, y));
          solidBlocks.getBlocks().setRectUnscaled(new Rectangle(x - Sizes.BLOCK, y + Tree.HEIGHT, Tree.WIDTH + Sizes.BLOCK, Sizes.BLOCK * 2), WallBlockType.DIRT);
        }
      } else {
        okBlocks = 0;
      }
      prevHeight = heights[i];
    }
  }

  void generateBats(Random random, int[] heights) {
    float DENSITY = configuration.getFloat("ObjectGenerator.BAT_DENSITY", 0.05f);
    if (DENSITY <= 0) return;
    int ALTITUDE = configuration.getInt("ObjectGenerator.BAT_ALTITUDE", 5);
    for (int i = 0; i < heights.length; i++) {
      if (random.nextFloat() < DENSITY) {
        Bat bat = new Bat(Sizes.MIN_X + Sizes.BLOCK * i, Sizes.MAX_Y - heights[i] - ALTITUDE * Sizes.BLOCK);
        if (solidBlocks.getBlocks().collidesWithNonEmpty(bat.getShape())) continue;
        bat.addTo(movingObjects, renderQueue, updateQueue);
      }
    }
  }

  void generateRocks(Random random, int[] heights) {
    float SMALL_ROCK_DENSITY = configuration.getFloat("ObjectGenerator.SMALL_ROCK_DENSITY", 0.1f);
    float LARGE_ROCK_DENSITY = configuration.getFloat("ObjectGenerator.LARGE_ROCK_DENSITY", 0.05f);
    assert SMALL_ROCK_DENSITY + LARGE_ROCK_DENSITY < 1;
    for (int i = 0; i < heights.length; i++) {
      float r = random.nextFloat();
      Boolean large = null;
      if (r < LARGE_ROCK_DENSITY) {
        large = true;
      } else {
        if (r < LARGE_ROCK_DENSITY + SMALL_ROCK_DENSITY) {
          large = false;
        }
      }
      if (large != null) {
        Rock rock = new Rock(Sizes.MIN_X + Sizes.BLOCK * i + Sizes.BLOCK / 2, Sizes.MAX_Y - heights[i], large);
        addToWorld(rock);
      }
    }
  }

  private void addToWorld(DroppableObject dropObject) {
    if (solidBlocks.isColliding(dropObject.getShape())) return;
    if (!movingObjects.add(dropObject, true)) return;
    renderQueue.add(dropObject);
  }
}
