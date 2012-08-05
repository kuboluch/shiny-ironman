package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.event.Event;
import kniemkiewicz.jqblocks.ingame.event.EventListener;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MouseInputInfo implements EventListener, Renderable {
  private int x;
  private int y;

  @Autowired
  PointOfView pointOfView;

  @Override
  public List<Class> getEventTypesOfInterest() {
    return Arrays.asList((Class) MouseMovedEvent.class);
  }

  @Override
  public void listen(List<Event> events) {
    List<MouseMovedEvent> mouseMovedEvents = Collections3.collect(events, MouseMovedEvent.class);

    if (!mouseMovedEvents.isEmpty()) {
      handleMouseMovedEvent(mouseMovedEvents);
    }
  }

  private void handleMouseMovedEvent(List<MouseMovedEvent> mouseMovedEvents) {
    assert mouseMovedEvents.size() > 0;
    MouseMovedEvent mme = mouseMovedEvents.get(0);
    x = mme.getNewLevelX();
    y = mme.getNewLevelY();
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.white);
    g.drawString("mouseX : " + x, 4, 25);
    g.drawString("mouseY : " + y, 4, 38);
  }
}
