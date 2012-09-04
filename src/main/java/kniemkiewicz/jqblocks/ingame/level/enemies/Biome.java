package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;

/**
 * User: knie
 * Date: 9/3/12
 */
public interface Biome {
  KillablePhysicalObject maybeGenerateNewEnemy(int delta, Player player);
}
