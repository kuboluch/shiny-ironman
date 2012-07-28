package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.World;

/**
 * User: knie
 * Date: 7/28/12
 */
public interface HasHealthPoints {
  HealthPoints getHp();
  void killed(World killer);
}
