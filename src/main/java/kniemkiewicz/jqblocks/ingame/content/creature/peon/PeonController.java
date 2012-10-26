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
  PathGraph pathGraph;

  @Autowired
  FreeFallController freeFallController;

  @Autowired
  CollisionController collisionController;


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


  private OnceXTimes<Peon> findPathClosure = new OnceXTimes<Peon>(90, true, new Closure<Peon>() {
    @Override
    public void run(Peon peon) {
      QuadTree.HasShape target = playerController.getPlayer();
      Position destination = pathGraph.getClosestPoint(GeometryUtils.getRectangleCenteredOn(target.getShape().getCenterX(), GeometryUtils.getMaxY(target.getShape()), Sizes.BLOCK * 3));
      if (peon.getCurrentPath() == null || destination == null) {
        // new Peon or sth
        Rectangle r = GeometryUtils.getRectangleCenteredOn(peon.getShape().getCenterX(), GeometryUtils.getMaxY(peon.getShape()), 24);
        Position origin = pathGraph.getClosestPoint(r);
        if (origin == null) {
          peon.setCurrentPath(null);
          return;
        }
        if (destination == null) {
          destination = origin;
        }
        peon.setCurrentPath(pathGraph.getPermPath(origin, destination));
      } else {
        peon.getCurrentPath().switchDestinationTo(destination);
      }
    }
  });

  @Override
  public void update(Peon object, int delta) {
    if (object.getCurrentPath() != null) {
      findPathClosure.maybeRunWith(object);
    } else {
      // We really need a path.
      findPathClosure.forceRun(object);
    }
    boolean outsideGraph = true;
    Path path = null;
    if (object.getCurrentPath() != null) {
      // This also updated "outsideGraph()"
      path = object.getCurrentPath().safeGetPath();
      outsideGraph = object.getCurrentPath().outsideGraph();
    }
    if (outsideGraph) {
      freeFallController.updateComplex(delta, null, object);
      object.setCurrentPath(null);
      return;
    }
    float speed = Peon.MAX_PEON_SPEED * delta;
    if (path != null) {
      object.getXYMovement().getYMovement().setSpeed(0);
      path.progressWithSpeed(speed);
      object.update();
    }
  }
}
