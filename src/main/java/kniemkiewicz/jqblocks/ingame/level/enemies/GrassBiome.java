package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.Zombie;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.level.VillageGenerator;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.background.Backgrounds;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.Direction;
import kniemkiewicz.jqblocks.ingame.util.WeightedPicker;
import kniemkiewicz.jqblocks.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

  public static Log logger = LogFactory.getLog(GrassBiome.class);

  enum MonsterType {
    ZOMBIE,
    BAT
  }

  WeightedPicker<Pair<MonsterType, Direction>> monsterPicker;

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

  @Autowired
  VillageGenerator villageGenerator;

  Random random = new Random();

  @PostConstruct
  void init() {
    monsterPicker = new WeightedPicker<Pair<MonsterType, Direction>>();
    configurePickerFor(MonsterType.BAT, "GrassBiome.BAT_EVERY_N_SEC", 5);
    configurePickerFor(MonsterType.ZOMBIE, "GrassBiome.ZOMBIE_EVERY_N_SEC", 15);
  }

  private void configurePickerFor(MonsterType type, String propertyName, float defaultEveryNSec) {
    float EVERY_N_SEC = configuration.getFloat(propertyName, defaultEveryNSec);
    assert EVERY_N_SEC >= 0;
    if (EVERY_N_SEC > 0) {
      monsterPicker.addChoice(0.5f / 1000 / EVERY_N_SEC, Pair.of(type, Direction.LEFT));
      monsterPicker.addChoice(0.5f / 1000 / EVERY_N_SEC, Pair.of(type, Direction.RIGHT));
    }
  }

  @Override
  public KillablePhysicalObject maybeGenerateNewEnemy(int delta, Player player) {
    Pair<MonsterType, Direction> p = monsterPicker.pick(delta);
    if (p != null) {
      switch (p.getFirst()) {
        case BAT:
          return generateBat(player, p.getSecond());
        case ZOMBIE:
          return generateZombie(player, p.getSecond());
        default:
          assert false;
      }
    }
    return null;
  }

  Bat generateBat(Player player, Direction direction) {
    // This is an approximation, won't work underground.
    Bat bat = new Bat(getMonsterX(player, direction), villageGenerator.getStartingY());
    moveAwayFromVillage(player, bat);
    updateBatY(player, bat);
    // By default bat goes right, but that should not be the case here.
    if (direction == Direction.RIGHT) {
      bat.getXYMovement().getXMovement().setSpeed(-bat.getXYMovement().getXMovement().getSpeed());
    }
    assert !solidBlocks.getBlocks().collidesWithNonEmpty(bat.getShape());
    if (bat.addTo(movingObjects, renderQueue, updateQueue)) {
      return bat;
    } else {
      return null;
    }
  }

  private float getMonsterX(Player player, Direction direction) {
    float distance = pointOfView.getWindowWidth() / 2;
    return player.getXYMovement().getX() + (direction == Direction.RIGHT ? distance : - distance);
  }

  private void updateBatY(Player player, Bat bat) {
    float height = 2 * Sizes.BLOCK;
    bat.getXYMovement().getYMovement().setPos(Sizes.MIN_Y + Bat.SIZE);
    bat.updateShape();
    float y = solidBlocks.getBlocks().getUnscaledDropHeight(bat.getShape()) - height;
    if (y > player.getXYMovement().getY()) {
      y = player.getXYMovement().getY();
    }
    // Exponential distribution.
    float randomHeight = (float) (- Math.log(1 - random.nextFloat()) * 4 * Sizes.BLOCK);
    assert randomHeight >= 0;
    y -= randomHeight;
    bat.getXYMovement().getYMovement().setPos(y);
    bat.updateShape();
  }

  private void moveAwayFromVillage(Player player, HasFullXYMovement monster) {
    while (RoamingEnemiesController.isNearFireplace(monster.getShape(), backgrounds)) {
      float dx = pointOfView.getWindowWidth();
      if (player.getShape().getCenterX() < VillageGenerator.STARTING_X) {
        dx = -dx;
      }
      monster.getXYMovement().getXMovement().setPos(monster.getXYMovement().getX() + dx);
      monster.updateShape();
    }
    if (monster.getXYMovement().getX() < Sizes.MIN_X + 300) {
      monster.getXYMovement().getXMovement().setPos(monster.getXYMovement().getX() + pointOfView.getWindowWidth());
      monster.updateShape();
    } else if (monster.getXYMovement().getX() > Sizes.MAX_X - 300) {
      monster.getXYMovement().getXMovement().setPos(monster.getXYMovement().getX() - pointOfView.getWindowWidth());
      monster.updateShape();
    }
  }

  Zombie generateZombie(Player player, Direction direction) {
    Zombie zombie = new Zombie(getMonsterX(player, direction), villageGenerator.getStartingY());
    moveAwayFromVillage(player, zombie);
    zombie.getXYMovement().getYMovement().setPos(Sizes.MIN_Y);
    zombie.updateShape();
    zombie.getXYMovement().getYMovement().setPos(solidBlocks.getBlocks().getUnscaledDropHeight(zombie.getShape()) - Sizes.BLOCK * 3);
    zombie.updateShape();
    if (zombie.addTo(movingObjects, renderQueue, updateQueue)) {
      return zombie;
    } else {
      return null;
    }
  }
}
