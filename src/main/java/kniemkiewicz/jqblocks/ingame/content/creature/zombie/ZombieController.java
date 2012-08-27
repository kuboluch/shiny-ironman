package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.FreeFallController;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ZombieController implements HealthController<Zombie>, UpdateQueue.UpdateController<Zombie> {

  @Autowired
  World world;

  @Autowired
  FreeFallController freeFallController;

  private static int TOUCH_DMG = 50;

  @Override
  public void killed(Zombie object, QuadTree.HasShape source) {
    world.killMovingObject(object);
  }

  @Override
  public void damaged(Zombie object, QuadTree.HasShape source, int amount) {
    ControllerUtils.pushFrom(object, source, ControllerUtils.DEFAULT_PUSH_BACK / 2);
  }

  @Override
  public void update(Zombie object, int delta) {
    freeFallController.updateComplex(delta, null, object);
    ControllerUtils.damageTouchedVillagers(world, object, TOUCH_DMG);
  }
}
