package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.object.ObjectKiller;

/**
 * User: knie
 * Date: 7/28/12
 */
public class HealthPoints {

  private int currentHp;
  private int maxHp;
  private HasHealthPoints object;

  public HealthPoints(int maxHp, HasHealthPoints object) {
    this(maxHp, maxHp, object);
  }

  public HealthPoints(int currentHp, int maxHp, HasHealthPoints object) {
    this.currentHp = currentHp;
    this.maxHp = maxHp;
    this.object = object;
  }

  public void damage(int dmg, ObjectKiller killer) {
    currentHp -= dmg;
    if (currentHp <= 0) {
      currentHp = 0;
      object.killed(killer);
    }
  }

  public int getCurrentHp() {
    return currentHp;
  }

  public int getMaxHp() {
    return maxHp;
  }
}
