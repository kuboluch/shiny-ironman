package kniemkiewicz.jqblocks.ingame.content.creature.peon;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.controller.*;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.*;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.closure.Closure;
import kniemkiewicz.jqblocks.ingame.util.closure.OnceXTimes;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static kniemkiewicz.jqblocks.ingame.content.creature.rabbit.RabbitDefinition.RABBIT_SLOWNESS;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
@Component
public class PeonController implements HealthController<Peon>, UpdateQueue.UpdateController<Peon> {

  public static Log logger = LogFactory.getLog(PeonController.class);

  @Autowired
  World world;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  UpdateQueue updateQueue;

  @Resource
  Sound newPeonSound;

  @Autowired
  SoundController soundController;

  @Autowired
  PlayerController playerController;

  @Autowired
  PeonMoveController peonMoveController;

  @Override
  public void killed(Peon object, QuadTree.HasShape source) {
    world.killMovingObject(object);
  }

  @Override
  public void damaged(Peon object, QuadTree.HasShape source, int amount) { }

  public boolean register(Peon peon) {
    if (!movingObjects.add(peon, false)) return false;
    renderQueue.add(peon);
    updateQueue.add(peon);
    soundController.play(newPeonSound);
    return true;
  }

  @Override
  public void update(Peon object, int delta) {
    if (object.getTarget() == null) {
      object.setTarget(playerController.getPlayer());
    }
    peonMoveController.update(object, delta);
  }
}
