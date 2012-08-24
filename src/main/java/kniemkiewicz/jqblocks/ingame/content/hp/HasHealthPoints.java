package kniemkiewicz.jqblocks.ingame.content.hp;

import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/28/12
 */
public interface HasHealthPoints<T extends HasHealthPoints> {
  HealthPoints getHp();
  BeanName<? extends HealthController<T>> getHealthController();
}
