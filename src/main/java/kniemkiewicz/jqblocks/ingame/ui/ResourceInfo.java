package kniemkiewicz.jqblocks.ingame.ui;

import kniemkiewicz.jqblocks.ingame.PlayerResourceController;
import kniemkiewicz.jqblocks.ingame.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceInfo implements Renderable {

  @Autowired
  PlayerResourceController playerResourceController;

  @Override
  public void render(Graphics g) {
    int y = 25;
    g.setColor(Color.white);
    for (String type : playerResourceController.getResourceTypes()) {
      g.drawString(type + " : " + playerResourceController.getResourceAmount(type), 300, y);
      y += 13;
    }
  }
}
