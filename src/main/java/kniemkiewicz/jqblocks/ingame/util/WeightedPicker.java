package kniemkiewicz.jqblocks.ingame.util;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * User: krzysiek
 * Date: 08.09.12
 */
public class WeightedPicker<T> {
  Random random = new Random();
  TreeMap<Float, T> choices = new TreeMap<Float, T>();

  public void addChoice(float probability, T value) {
    if (choices.size() == 0) {
      choices.put(probability, value);
    } else {
      float max = choices.lastKey();
      assert probability + max < 1.;
      choices.put(probability + max, value);
    }
  }

  public T pick(float multiplier) {
    if (choices.size() == 0) return null;
    assert choices.lastKey() * multiplier < 1;
    float x = random.nextFloat() / multiplier;
    if (x >= choices.lastKey()) return null;
    return choices.higherEntry(x).getValue();
  }

  public T pick() {
    return pick(1);
  }

  public boolean empty() {
    return choices.size() == 0;
  }

  public static <E> WeightedPicker<E> createUniformPicker(List<E> choices, float totalProbability) {
    WeightedPicker<E> picker = new WeightedPicker<E>();
    for (E c : choices) {
      picker.addChoice(totalProbability / choices.size(), c);
    }
    return picker;
  }
}
