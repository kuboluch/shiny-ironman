package kniemkiewicz.jqblocks.ingame.level;

import kniemkiewicz.jqblocks.ingame.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 3/29/13
 */
@Component
public class SavegameLevelGenerator implements LevelGenerator{

  @Autowired
  World world;

  @Override
  public void generate(Settings settings) {
    assert settings.savegame != null;
    world.loadGameData(settings.savegame);
  }
}
