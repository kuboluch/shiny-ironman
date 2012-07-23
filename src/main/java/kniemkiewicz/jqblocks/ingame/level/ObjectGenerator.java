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
    assert Tree.WIDTH / Sizes.BLOCK == 2;
    float TREE_DENSITY = configuration.getFloat("ObjectGenerator.TREE_DENSITY", 0.5f);
    int prevHeight = heights[0];
    boolean treePut = false;
    for (int i = 1; i < heights.length; i++) {
      if ((heights[i] == prevHeight) && !treePut && (random.nextFloat() < TREE_DENSITY)) {
        backgrounds.add(backgrounds.getTree(Sizes.MIN_X + (i - 1) * Sizes.BLOCK, Sizes.MAX_Y -  heights[i] - Tree.HEIGHT));
        treePut = true;
      } else {
        treePut = false;
      }
      prevHeight = heights[i];
    }
  }
}
