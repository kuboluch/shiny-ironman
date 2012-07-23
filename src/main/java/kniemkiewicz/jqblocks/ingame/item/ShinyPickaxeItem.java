package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.item.ShinyPickaxeItemController;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//TODO: Items should not be components. Split rendering to separate class?
@Component
public class ShinyPickaxeItem implements Item {

  @Resource(name = "pickaxeImage")
  private Image image;

  public void renderItem(Graphics g, int x, int y, int square_size) {
    image.draw(x, y, square_size, square_size);
  }

  public Class<? extends ItemController> getController() {
    return ShinyPickaxeItemController.class;
  }
}
