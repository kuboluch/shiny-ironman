package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ZombieController implements HealthController<Zombie>, UpdateQueue.UpdateController<Zombie> {
  @Override
  public void killed(Zombie object, QuadTree.HasShape source) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void damaged(Zombie object, QuadTree.HasShape source, int amount) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void update(Zombie object, int delta) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
