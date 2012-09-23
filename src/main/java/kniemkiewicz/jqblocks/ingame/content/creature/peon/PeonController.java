package kniemkiewicz.jqblocks.ingame.content.creature.peon;

import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.controller.SoundController;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
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

  @Autowired
  SoundController soundController;

  @Override
  public void killed(Peon object, QuadTree.HasShape source) {
    world.killMovingObject(object);
  }

  @Override
  public void damaged(Peon object, QuadTree.HasShape source, int amount) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean register(Peon peon) {
    if (!movingObjects.add(peon, true)) return false;
    renderQueue.add(peon);
    soundController.play(newPeonSound);
    return true;
  }
}
