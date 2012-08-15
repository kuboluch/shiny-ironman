package kniemkiewicz.jqblocks.ingame.ui.info;

import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.resource.ResourceStorageController;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ResourceInfo implements Renderable {

  @Autowired
  ResourceStorageController resourceStorageController;

  @Resource ( name="blockSheet" )
  XMLPackedSheet blockSheet;

  @Override
  public void render(Graphics g) {
    int y = 25;
    g.setColor(Color.white);
    for (ResourceType type : resourceStorageController.getResourceTypes()) {
      int imageSize = 16;
      int imageMargin = 2;
      Image image = blockSheet.getSprite("wood");
      g.drawImage(image, 300 + imageMargin, y + imageMargin,
          300 + imageMargin + imageSize, y + imageMargin + imageSize, 0, 0, image.getWidth(), image.getHeight());
      g.drawString(type.getName() + " : " + resourceStorageController.getResourceAmount(type), 325, y);
      y += 13;
    }
  }
}
