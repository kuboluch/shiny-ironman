package kniemkiewicz.jqblocks.ingame.object.hp;

import kniemkiewicz.jqblocks.ingame.World;

import java.io.Serializable;

/**
 * User: knie
 * Date: 7/28/12
 */
public class HealthPoints implements Serializable {

  private static final long serialVersionUID = 1;

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

  public void damage(int dmg, World killer) {
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
