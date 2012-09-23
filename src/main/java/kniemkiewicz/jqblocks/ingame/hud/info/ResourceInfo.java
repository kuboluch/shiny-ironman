package kniemkiewicz.jqblocks.ingame.hud.info;

import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.resource.renderer.ResourceRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceInfo implements Renderable {

  @Autowired
  ResourceStorageController resourceStorageController;

  @Autowired
  ResourceRenderer resourceRenderer;

  @Override
  public void render(Graphics g) {
    int y = 25;
    g.setColor(Color.white);
    for (ResourceType type : ResourceType.values()) {
      int imageSize = 16;
      int imageMargin = 2;
      Image image = resourceRenderer.getImage(type);
      g.drawImage(image, 300 + imageMargin, y + imageMargin,
          300 + imageMargin + imageSize, y + imageMargin + imageSize, 0, 0, image.getWidth(), image.getHeight());
      g.drawString(type.getName() + " : " + resourceStorageController.getResourceAmount(type), 325, y);
      y += 18;
    }
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
