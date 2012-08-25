package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.util.FullXYMovement;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public interface HasFullXYMovement extends QuadTree.HasShape{
  FullXYMovement getFullXYMovement();

  void updateShape();
}
