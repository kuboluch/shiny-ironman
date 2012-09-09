package kniemkiewicz.jqblocks.ingame.level.enemies;

import java.util.Random;
import java.util.TreeMap;

/**
 * User: krzysiek
 * Date: 08.09.12
 */
public class WeightedPicker<T> {
  Random random = new Random();
  TreeMap<Float, T> choices = new TreeMap<Float, T>();

  void addChoice(float probability, T value) {
    if (choices.size() == 0) {
      choices.put(probability, value);
    } else {
      float max = choices.lastKey();
      assert probability + max < 1.;
      choices.put(probability + max, value);
    }
  }

  T pick(float multiplier) {
    if (choices.size() == 0) return null;
    assert choices.lastKey() * multiplier < 1;
    float x = random.nextFloat() / multiplier;
    if (x >= choices.lastKey()) return null;
    return choices.higherEntry(x).getValue();
  }

  T pick() {
    return pick(1);
  }

  boolean empty() {
    return choices.size() == 0;
  }
}
