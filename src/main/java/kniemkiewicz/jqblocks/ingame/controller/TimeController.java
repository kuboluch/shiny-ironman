package kniemkiewicz.jqblocks.ingame.controller;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import kniemkiewicz.jqblocks.util.Pair;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 08.11.12
 *
 * Stores current game time and allows registering event that should be triggered after some delay or in repeatably
 * fashion.
 * Currently this time and events are not serialized correctly but they will be.
 */
@Component
public class TimeController {

  public interface Event extends Serializable{
    void execute(long currentTime);
  }

  long currentTimeMs = 0;

  final TreeMultimap<Long, Event> eventQueue = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
  // Filled by events added while we are iterating over eventQueue. Merged on each update.
  List<Pair<Long, Event>> newEvents = new ArrayList<Pair<Long, Event>>();
  // If true, eventQueue should not be modified.
  boolean eventQueueLocked = false;

  public void update(long delta) {
    currentTimeMs += delta;
    for (Pair<Long, Event> p : newEvents) {
      eventQueue.put(p.getFirst(), p.getSecond());
    }
    newEvents.clear();
    eventQueueLocked = true;
    Iterator<Long> it = eventQueue.keySet().iterator();
    while (it.hasNext()) {
      Long time = it.next();
      if (time > currentTimeMs) break;
      for (Event e : eventQueue.get(time)) {
        e.execute(currentTimeMs);
      }
      it.remove();
    }
    eventQueueLocked = false;
  }

  public long getTime() {
    return currentTimeMs;
  }

  public void executeOnceAt(long time, Event e) {
    if (eventQueueLocked) {
      newEvents.add(Pair.of(time, e));
    } else {
      eventQueue.put(time, e);
    }
  }

  public void executeOnceAfter(long delay, Event e) {
    executeOnceAt(currentTimeMs + delay, e);
  }

  public void executeRepeatableAt(long time, final long repetitionDelay, final Event e) {
    Event repeatableEvent = new Event() {
      @Override
      public void execute(long currentTime) {
        e.execute(currentTime);
        executeOnceAt(currentTime + repetitionDelay, this);
      }
    };
    executeOnceAt(time, repeatableEvent);
  }
}
