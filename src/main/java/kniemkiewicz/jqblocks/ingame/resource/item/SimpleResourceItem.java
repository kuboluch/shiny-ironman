package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.renderer.ResourceRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
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
  public void renderItem(Graphics g, int x, int y, int square_size) { }

  @Override
  public Class<? extends ItemController> getItemController() {
    return ResourceItemController.class;
  }

  private static final BeanName<ResourceRenderer> RENDERER = new BeanName<ResourceRenderer>(ResourceRenderer.class, "resourceRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
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
