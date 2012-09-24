package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
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

  static final BeanName<SimpleImageRenderer> RENDERER = new BeanName<SimpleImageRenderer>(SimpleImageRenderer.class, "fireballItemRenderer");

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
