package kniemkiewicz.jqblocks.ingame.level;

import java.io.ObjectInputStream;

/**
 * User: knie
 * Date: 3/29/13
 **/
public interface LevelGenerator {
  static public class Settings {
    // If not null, level will be generated using it. Otherwise some pseudorandom seed is chosen.
    public Long seed = null;
    // If not null, level will be loaded from this stream. Otherwise it will be generated randomly.
    public ObjectInputStream savegame = null;
  }

  void generate(Settings settings);
}
