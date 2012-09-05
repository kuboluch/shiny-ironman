package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * User: knie
 * Date: 7/20/12
 * Time: 9:40 PM
 */
@Component
public class TimingInfo implements Renderable {

  public static String RENDER_TIMER = "render";
  public static TimingInfo CURRENT_TIMING_INFO = null;

  static {
    CURRENT_TIMING_INFO = new TimingInfo();
  }

  @PostConstruct
  void init() {
    CURRENT_TIMING_INFO = this;
  }

  //TODO: fix both classes to check time only on render.
  public static class Timer {
    long startTime = 0;
    long displayTime = 0;
    long displayCount = 0;
    long lastUpdate = 0;
    long currentSum = 0;
    long currentCount = 0;

    Timer() {
      reset();
    }

    public final void reset() {
      startTime = nanoToMicro(System.nanoTime());
    }

    public final long nanoToMicro(long nano) {
      if (nano < 0) {
        return nano / 1000 - 1;
      } else {
        return nano / 1000;
      }
    }

    public final void record() {
      long current = nanoToMicro(System.nanoTime());
      currentSum += current - startTime;
      currentCount++;
      if (current - lastUpdate > 1000000) {
        displayTime = currentSum / currentCount;
        displayCount = currentCount;
        currentSum = 0;
        currentCount = 0;
        lastUpdate = current;
      }
    }
  }

  public static class IntCounter {
    long displayCount = 0;
    long displayValue = 0;
    long lastUpdate = 0;
    long currentSum = 0;
    long currentCount = 0;

    public final void record(int value) {
      currentSum += value;
      currentCount++;
      long current = System.currentTimeMillis();
      if (current - lastUpdate > 1000) {
        displayCount = currentSum;
        displayValue = currentCount / displayCount;
        currentSum = 0;
        currentCount = 0;
        lastUpdate = current;
      }
    }
  }


  Map<String, Timer> timers = new HashMap<String, Timer>();
  Map<String, IntCounter> counters = new HashMap<String, IntCounter>();

  public final Timer getTimer(String name) {
    if (!timers.containsKey(name))  {
       timers.put(name, new Timer());
    }
    Timer t = timers.get(name);
    t.reset();
    return t;
  }

  public final IntCounter getCounter(String name) {
    if (!counters.containsKey(name)) {
      counters.put(name, new IntCounter());
    }
    return counters.get(name);
  }

  @Override
  public final void render(Graphics g) {
    int y = 77;
    g.setColor(Color.white);
    if (!timers.containsKey(RENDER_TIMER)) return;
    float frameCount = (float)timers.get(RENDER_TIMER).displayCount;
    if (frameCount == 0) return;
    for (String name : timers.keySet()) {
      g.drawString(name + " : " + Math.round(timers.get(name).displayCount  / frameCount) + "x" + timers.get(name).displayTime, 4, y);
      y += 14;
    }
    for (String name : counters.keySet()) {
      g.drawString(name + " : " + Math.round(counters.get(name).displayCount  / frameCount) + "x" + counters.get(name).displayValue, 4, y);
      y += 14;
    }
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
