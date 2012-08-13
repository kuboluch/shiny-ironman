package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
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
    for (ResourceType type : resourceStorageController.getResourceTypes()) {
      g.drawString(type.getName() + " : " + resourceStorageController.getResourceAmount(type), 300, y);
      y += 13;
    }
  }
}
