package kniemkiewicz.jqblocks.ingame.event;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.util.Collections3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class EventBus {

  final List<EventListener> listeners = new ArrayList<EventListener>();

  final Object lock = new Object();

  List<Event> events = new ArrayList<Event>();

  @Autowired
  PointOfView pov;

  // Used in code to find out where is the mouse currently.
  MouseMovedEvent latestMouseMovedEvent;

  @PostConstruct
  void init() {
    // TODO: this is broken until first event is received
    latestMouseMovedEvent = new MouseMovedEvent(0,0,0,0,0,0,pov.getWindowWidth() / 2, pov.getWindowHeight() / 2);
  }

  public void addListener(EventListener eventListener) {
    listeners.add(eventListener);
  }

  public void broadcast(Event event) {
    synchronized (lock) {
      events.add(event);
    }
  }

  public void broadcast(List<? extends Event> events) {
    synchronized (lock) {
      this.events.addAll(events);
    }
  }

  public void update() {
    List<Event> eventsForListeners;
    synchronized (lock) {
      eventsForListeners = events;
      events = new ArrayList<Event>();
    }
    for (Event event : eventsForListeners) {
      if (event instanceof MouseMovedEvent) {
        latestMouseMovedEvent = (MouseMovedEvent) event;
      }
    }
    for (EventListener listener : listeners) {
      List<Event> eventsOfIntrest = new ArrayList<Event>();
      List<Class> eventTypes = listener.getEventTypesOfInterest();
      for (Class eventType : eventTypes) {
        eventsOfIntrest.addAll(Collections3.collectSubclasses(eventsForListeners, eventType));
      }
      Iterator<Event> iter = eventsOfIntrest.iterator();
      while (iter.hasNext()) {
        if (iter.next().isConsumed()) {
          iter.remove();
        }
      }
      if (!eventsOfIntrest.isEmpty()) {
        listener.listen(eventsOfIntrest);
      }
    }
  }

  public MouseMovedEvent getLatestMouseMovedEvent() {
    return latestMouseMovedEvent;
  }
}
