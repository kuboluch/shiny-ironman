package kniemkiewicz.jqblocks.ingame.content.item.spell;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.springframework.stereotype.Component;

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
    return null;
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
