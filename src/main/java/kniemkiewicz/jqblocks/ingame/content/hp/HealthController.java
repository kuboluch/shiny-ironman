package kniemkiewicz.jqblocks.ingame.content.hp;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public interface HealthController<T extends HasHealthPoints> {
  void killed(T object);
  void damaged(T object, Object source, int amount);
}
