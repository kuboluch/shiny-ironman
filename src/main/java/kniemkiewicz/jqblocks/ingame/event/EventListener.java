package kniemkiewicz.jqblocks.ingame.event;

import java.util.List;

public interface EventListener {

  public void listen(List<Event> events);

  public List<Class> getEventTypesOfInterest();

}
