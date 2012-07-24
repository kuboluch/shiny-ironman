package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.Tree;
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
  Configuration configuration;

  void generateTrees(Random random, int[] heights) {
    float TREE_DENSITY = configuration.getFloat("ObjectGenerator.TREE_DENSITY", 0.7f);
    int REQUIRED_OK_BLOCKS = Tree.WIDTH / Sizes.BLOCK - 1;
    int prevHeight = heights[0];
    int okBlocks = 0;
    for (int i = 1; i < heights.length; i++) {
      if (heights[i] == prevHeight) {
        okBlocks++;
        if ((okBlocks == REQUIRED_OK_BLOCKS) && (random.nextFloat() < TREE_DENSITY)) {
          okBlocks = -1; // Let the trees have at least one block between them.
          backgrounds.add(backgrounds.getTree(Sizes.MIN_X + (i + 1) * Sizes.BLOCK - Tree.WIDTH, Sizes.MAX_Y -  heights[i] - Tree.HEIGHT));
        }
      } else {
        okBlocks = 0;
      }
      prevHeight = heights[i];
    }
  }
}
