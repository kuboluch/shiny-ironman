package kniemkiewicz.jqblocks.ingame.content.item.spell;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
public class FastTravelItem implements Item {
  @Override
  public Class<? extends ItemController> getItemController() {
    return FastTravelItemController.class;
  }

  static final BeanName<FastTravelItemRenderer> RENDERER = new BeanName<FastTravelItemRenderer>(FastTravelItemRenderer.class);

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends EquippedItemRenderer> getEquippedItemRenderer() {
    return EmptyItemRenderer.RENDERER;
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
