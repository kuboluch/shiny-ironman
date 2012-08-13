package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/27/12
 */
public class SimpleResourceItem implements ResourceItem {

  final Resource resource;

  public SimpleResourceItem(Resource resource) {
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
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return null;
  }

  @Override
  public boolean isLarge() {
    return true;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public Resource getResource() {
    return resource;
  }
}
