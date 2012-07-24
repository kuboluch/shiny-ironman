package kniemkiewicz.jqblocks.ingame.event;

import java.util.List;

public interface EventListener {

  void listen(List<Event> events);

  List<Class> getEventTypesOfIntrest();

}
