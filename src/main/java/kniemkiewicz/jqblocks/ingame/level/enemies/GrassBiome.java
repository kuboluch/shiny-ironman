package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * User: knie
 * Date: 9/3/12
 */
@Component
public class GrassBiome implements Biome{

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  Configuration configuration;

  @Autowired
  PointOfView pointOfView;

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  UpdateQueue updateQueue;

  @Autowired
  Backgrounds backgrounds;

  Random random = new Random();

  float BAT_EVERY_N_SEC = -1;

  @PostConstruct
  void init() {
    BAT_EVERY_N_SEC = configuration.getFloat("GrassBiome.BAT_EVERY_N_SEC", 3);
  }

  @Override
  public KillablePhysicalObject maybeGenerateNewEnemy(int delta, Player player) {
    assert BAT_EVERY_N_SEC >= 0;
    if (BAT_EVERY_N_SEC == 0) return null;
    float probability = delta / 1000f / BAT_EVERY_N_SEC;
    float rnd = random.nextFloat();
    if (rnd < probability) {
      return generateBat(player, rnd < probability / 2);
    }
    return null;
  }

  Bat generateBat(Player player, boolean rightSide) {
    float distance = pointOfView.getWindowWidth() / 2 + Sizes.BLOCK * 5;
    float x = player.getXYMovement().getX() + (rightSide ? distance : - distance);
    float height = 5 * Sizes.BLOCK;
    // This is an approximation, won't work underground.
    Bat bat = new Bat(x, Sizes.MIN_Y);
    float y = solidBlocks.getBlocks().getUnscaledDropHeight(bat.getShape()) - height;
    bat.getXYMovement().getYMovement().setPos(y);
    bat.updateShape();

    while (RoamingEnemiesController.isNearFireplace(bat.getShape(), backgrounds)) {
      float dx = pointOfView.getWindowWidth();
      if (player.getShape().getCenterX() < VillageGenerator.STARTING_X) {
        dx = -dx;
      }
      bat.getXYMovement().getXMovement().setPos(bat.getXYMovement().getX() + dx);
      y = solidBlocks.getBlocks().getUnscaledDropHeight(bat.getShape()) - height;
      bat.getXYMovement().getYMovement().setPos(y);
      bat.updateShape();
    }
    // By default bat go right, but that should not be the case here.
    if (rightSide) {
      bat.getXYMovement().getXMovement().setSpeed(-bat.getXYMovement().getXMovement().getSpeed());
    }
    if (bat.addTo(movingObjects, renderQueue, updateQueue)) {
      return bat;
    } else {
      return null;
    }
  }
}
