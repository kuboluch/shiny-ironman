package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

  public static Log logger = LogFactory.getLog(RoamingEnemiesController.class);

  @Autowired
  PlayerController playerController;

  @Autowired
  Backgrounds backgrounds;

  @Autowired
  TownBiome townBiome;

  @Autowired
  GrassBiome grassBiome;

  @Autowired
  World world;

  @Autowired
  Configuration configuration;

  @Autowired
  PointOfView pointOfView;

  static final float MAX_PURSUIT_DISTANCE_SCREENS = 1;
  int MAX_ACTIVE_ENEMIES = -1;
  float TOWN_RADIUS_FROM_FIREPLACE = -1;

  @PostConstruct
  void init() {
    MAX_ACTIVE_ENEMIES = configuration.getInt("RoamingEnemiesController.MAX_ACTIVE_ENEMIES", 10);
    TOWN_RADIUS_FROM_FIREPLACE = configuration.getFloat("RoamingEnemiesController.TOWN_RADIUS_FROM_FIREPLACE", 500);
  }

  List<KillablePhysicalObject> activeEnemies = new LinkedList<KillablePhysicalObject>();

  // We cache biome, until next call to update()
  Biome cachedBiome = null;

  public Biome getCurrentBiome() {
    if (cachedBiome == null) {
      cachedBiome = calculateCurrentBiome();
      assert cachedBiome != null;
    }
    return cachedBiome;
  }

  private Biome calculateCurrentBiome() {
    assert TOWN_RADIUS_FROM_FIREPLACE >= 0;
    // "fireplace" should be a valid bean name
    assert world.getSpringBeanProvider().getBean(new BeanName<WorkplaceDefinition>(WorkplaceDefinition.class, "fireplace"), false) != null;
    Rectangle aroundPlayer = GeometryUtils.getRectangleCenteredOn(playerController.getPlayer().getShape(),
        2 * TOWN_RADIUS_FROM_FIREPLACE, 2 * TOWN_RADIUS_FROM_FIREPLACE);
    Iterator<BackgroundElement> it = backgrounds.intersects(aroundPlayer);
    while (it.hasNext()) {
      BackgroundElement el = it.next();
      if (el instanceof WorkplaceBackgroundElement) {
        WorkplaceBackgroundElement wbe = (WorkplaceBackgroundElement) el;
        if (wbe.getWorkplace().getBeanName().equals("fireplace")) {
          return townBiome;
        }
      }
    }
    return grassBiome;
  }

  private boolean isTooFar(Shape player, Shape object) {
    return Math.abs(player.getCenterX() - object.getCenterX()) > MAX_PURSUIT_DISTANCE_SCREENS * pointOfView.getWindowWidth()
        || Math.abs(player.getCenterY() - object.getCenterY()) > MAX_PURSUIT_DISTANCE_SCREENS * pointOfView.getWindowHeight();
  }


  public void update(int delta) {
    assert MAX_ACTIVE_ENEMIES >= 0;
    cachedBiome = null;
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
