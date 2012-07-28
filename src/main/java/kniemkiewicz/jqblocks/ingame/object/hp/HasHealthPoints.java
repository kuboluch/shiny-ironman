package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.object.ObjectKiller;

/**
 * User: knie
 * Date: 7/28/12
 */
public interface HasHealthPoints {
  HealthPoints getHp();
  void killed(ObjectKiller killer);
}
