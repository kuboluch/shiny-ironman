package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: knie
 * Date: 9/3/12
 */
@Component
public final class RoamingEnemiesController {

  @Autowired
  PlayerController playerController;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  TownBiome townBiome;

  @Autowired
  World world;

  @Autowired
  Configuration configuration;

  @Autowired
  PointOfView pointOfView;

  static final float MAX_PURSUIT_DISTANCE_SCREENS = 1;
  int MAX_ACTIVE_ENEMIES = 0;

  @PostConstruct
  void init() {
    MAX_ACTIVE_ENEMIES = configuration.getInt("RoamingEnemiesController.MAX_ACTIVE_ENEMIES", 10);
  }

  List<KillablePhysicalObject> activeEnemies = new LinkedList<KillablePhysicalObject>();

  public Biome getCurrentBiome() {
    return townBiome;
  }

  private boolean isTooFar(Shape player, Shape object) {
    return Math.abs(player.getCenterX() - object.getCenterX()) > MAX_PURSUIT_DISTANCE_SCREENS * pointOfView.getWindowWidth()
        || Math.abs(player.getCenterY() - object.getCenterY()) > MAX_PURSUIT_DISTANCE_SCREENS * pointOfView.getWindowHeight();
  }


  public void update(int delta) {
    Rectangle playerShape = playerController.getPlayer().getShape();
    Iterator<KillablePhysicalObject> it = activeEnemies.iterator();
    while (it.hasNext()) {
      KillablePhysicalObject ob = it.next();
      if (ob.getHp().isDead() || isTooFar(playerShape, ob.getShape())) {
        it.remove();
      }
    }
    if (activeEnemies.size() < MAX_ACTIVE_ENEMIES) {
      KillablePhysicalObject ob = getCurrentBiome().maybeGenerateNewEnemy(delta, playerController.getPlayer());
      if (ob != null) {
        activeEnemies.add(ob);
      }
    }
  }
}
