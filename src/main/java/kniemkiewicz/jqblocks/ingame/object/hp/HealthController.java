package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.util.QuadTree;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public interface HealthController<T extends HasHealthPoints> {
  void killed(T object, QuadTree.HasShape source);
  void damaged(T object, QuadTree.HasShape source, int amount);
}
