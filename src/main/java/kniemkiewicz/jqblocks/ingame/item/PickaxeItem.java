package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.controller.item.PickaxeItemController;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
//TODO: Items should not be components. Split rendering to separate class?
@Component
public class PickaxeItem implements Item {

  @Resource(name = "pickaxeImage")
  private Image image;

  public void renderItem(Graphics g, int x, int y, int square_size) {
    image.draw(x,y,square_size, square_size);
  }

  public Class<? extends ItemController> getController() {
    return PickaxeItemController.class;
  }
}
