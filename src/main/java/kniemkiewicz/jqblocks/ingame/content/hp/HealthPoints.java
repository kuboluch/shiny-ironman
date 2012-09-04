package kniemkiewicz.jqblocks.ingame.content.hp;

import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;

import java.io.Serializable;
import java.util.WeakHashMap;

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

  public void damage(int dmg, QuadTree.HasShape source, World world) {
    currentHp -= dmg;
    if (currentHp <= 0) {
      currentHp = 0;
      world.getSpringBeanProvider().<HealthController>getBean(object.getHealthController(), true).killed(object, source);
    } else {
      world.getSpringBeanProvider().<HealthController>getBean(object.getHealthController(), true).damaged(object, source, dmg);
    }
  }

  public int getCurrentHp() {
    return currentHp;
  }

  public int getMaxHp() {
    return maxHp;
  }

  public boolean isDead() {
    return currentHp <= 0;
  }

  transient WeakHashMap<Object, Long> attackers;

  public void damageRateLimited(QuadTree.HasShape attacker, int biteDmg, long delta, World world) {
    if (attackers == null) {
      attackers = new WeakHashMap<Object, Long>();
    }
    long currentTime = world.getTimestamp();
    boolean doDmg = false;
    if (attackers.containsKey(attacker)) {
      if (currentTime - attackers.get(attacker) > delta) {
        doDmg = true;
      }
    } else {
      doDmg = true;
    }
    if (doDmg) {
      this.damage(biteDmg, attacker, world);
      attackers.put(attacker, currentTime);
    }
  }
}
