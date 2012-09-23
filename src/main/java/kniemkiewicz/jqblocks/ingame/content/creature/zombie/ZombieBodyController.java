package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 11.09.12
 */
@Component
public class ZombieBodyController implements UpdateQueue.UpdateController<ZombieBody>{

  @Override
  public void update(ZombieBody object, int delta) {
    object.setAge(object.getAge() + delta);
  }
}
