package kniemkiewicz.jqblocks.ingame.controller.event;

import java.util.List;

public interface EventListener {

  public List<Class> getEventTypesOfInterest();

  public void listen(List<Event> events);

}
