package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.content.item.spell.FastTravelItemController;
import kniemkiewicz.jqblocks.ingame.content.item.spell.FastTravelItemRenderer;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 16.09.12
 */
public class FireballItem implements Item {
  @Override
  public Class<? extends ItemController> getItemController() {
    return FireballItemController.class;
  }

  static final BeanName<ImageRendererImpl> RENDERER = new BeanName<ImageRendererImpl>(ImageRendererImpl.class, "fireballRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  static final BeanName<FireballEquippedItemRenderer> EQUIPPED_RENDERER = new BeanName<FireballEquippedItemRenderer>(FireballEquippedItemRenderer.class);

  @Override
  public BeanName<? extends EquippedItemRenderer> getEquippedItemRenderer() {
    return EQUIPPED_RENDERER;
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
