package kniemkiewicz.jqblocks.ingame.content.creature.peon;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import org.newdawn.slick.Sound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
@Component
public class PeonController implements HealthController<Peon> {

  @Autowired
  World world;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Resource
  Sound newPeonSound;

  @Override
  public void killed(Peon object) {
    world.killMovingObject(object);
  }

  @Override
  public void damaged(Peon object, Object source, int amount) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean register(Peon peon) {
    if (!movingObjects.add(peon)) return false;
    renderQueue.add(peon);
    newPeonSound.play();
    return true;
  }
}
