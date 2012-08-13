package kniemkiewicz.jqblocks.ingame.event.input;

import kniemkiewicz.jqblocks.ingame.event.AbstractEvent;

import java.util.Comparator;

public class InputEvent extends AbstractEvent {

  private long time;

  public InputEvent() {
    this.time = System.nanoTime();
  }

  public long getTime() {
    return time;
  }

  private long nanoToMicro(long nano) {
    if (nano < 0) {
      return nano / 1000 - 1;
    } else {
      return nano / 1000;
    }
  }

  public static class TimeComparator implements Comparator<InputEvent> {
    @Override
    public int compare(InputEvent o1, InputEvent o2) {
      if (o1.time < o2.time) {
        return -1;
      }
      if (o1.time > o2.time) {
        return 1;
      }
      return 0;
    }
  }
}
