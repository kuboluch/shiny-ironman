package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/21/12
 */
public class BowItem implements Item {

  @Override
  public Class<? extends ItemController> getItemController() {
    return BowItemController.class;
  }

  private static final BeanName<BowRenderer> RENDERER = new BeanName<BowRenderer>(BowRenderer.class);

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends Renderable> getEquippedItemRenderer() {
    return RENDERER;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
