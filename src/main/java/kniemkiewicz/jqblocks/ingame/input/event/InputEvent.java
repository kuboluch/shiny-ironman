package kniemkiewicz.jqblocks.ingame.input.event;

import java.util.Comparator;

public class InputEvent {

  private long time;

  private boolean processed = false;

  public InputEvent() {
    this.time = System.nanoTime();
  }

  public long getTime() {
    return time;
  }

  public boolean isProcessed() {
    return processed;
  }

  public void markProcessed(boolean processed) {
    this.processed = true;
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
