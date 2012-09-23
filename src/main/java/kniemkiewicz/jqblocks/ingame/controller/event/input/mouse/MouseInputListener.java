package kniemkiewicz.jqblocks.ingame.controller.event.input.mouse;

import kniemkiewicz.jqblocks.ingame.controller.event.input.InputEvent;

import java.util.List;

public interface MouseInputListener {

  void listen(List<InputEvent> events);

}
