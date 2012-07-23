package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.input.MouseInputListener;
import kniemkiewicz.jqblocks.ingame.input.event.InputEvent;
import kniemkiewicz.jqblocks.ingame.input.event.MouseMovedEvent;
import kniemkiewicz.jqblocks.util.Collections3;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MouseInputInfo implements MouseInputListener, Renderable {

  private int x;
  private int y;

  @Autowired
  PointOfView pointOfView;

  @Override
  public void listen(List<InputEvent> events) {
    List<MouseMovedEvent> mousePressedEvents = Collections3.collect(events, MouseMovedEvent.class);

    if (!mousePressedEvents.isEmpty()) {
      handleMouseMovedEvent(mousePressedEvents);
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
    g.drawString("mouseX : " + x, 150, 25);
    g.drawString("mouseY : " + y, 150, 38);
  }
}
