package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.util.QuadTree;

/**
 * User: knie
 * Date: 8/25/12
 */
public interface HasSource extends QuadTree.HasShape{
  QuadTree.HasShape getSource();
}
