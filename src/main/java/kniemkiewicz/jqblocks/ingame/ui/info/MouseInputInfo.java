package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.ingame.event.EventBus;
import kniemkiewicz.jqblocks.ingame.event.input.mouse.MouseMovedEvent;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MouseInputInfo implements Renderable {

  @Autowired
  EventBus eventBus;

  @Override
  public void render(Graphics g) {
    g.setColor(Color.white);
    MouseMovedEvent ev = eventBus.getLatestMouseMovedEvent();
    g.drawString("mouseX : " + ev.getNewLevelX(), 4, 25);
    g.drawString("mouseY : " + ev.getNewLevelY(), 4, 38);
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
