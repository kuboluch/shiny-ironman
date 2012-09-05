package kniemkiewicz.jqblocks.ingame.level.enemies;

import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 9/3/12
 */
@Component
public class GrassBiome implements Biome{
  @Override
  public KillablePhysicalObject maybeGenerateNewEnemy(int delta, Player player) {
    return null;
  }
}
