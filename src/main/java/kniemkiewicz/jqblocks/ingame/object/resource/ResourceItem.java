package kniemkiewicz.jqblocks.ingame.object.resource;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/27/12
 */
public class ResourceItem implements Item {

  final Resource resource;

  public ResourceItem(Resource resource) {
    this.resource = resource;
  }

  @Override
  public void renderItem(Graphics g, int x, int y, int square_size) {
    // TODO: same as in resourceObject.
    g.setColor(Color.cyan);
    g.fillRect(x + 3, y + 3, square_size - 6, square_size - 6);
  }

  @Override
  public Class<? extends ItemController> getItemController() {
    return ResourceItemController.class;
  }

  @Override
  public boolean isLarge() {
    return true;
  }
}
