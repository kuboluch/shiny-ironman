package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.PlayerResourceController;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceInfo implements Renderable {

  @Autowired
  ResourceStorageController resourceStorageController;

  @Override
  public void render(Graphics g) {
    int y = 25;
    g.setColor(Color.white);
    for (String type : resourceStorageController.getResourceTypes()) {
      g.drawString(type + " : " + resourceStorageController.getResourceAmount(type), 300, y);
      y += 13;
    }
  }
}
