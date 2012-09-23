package kniemkiewicz.jqblocks.ingame.content.item.pickaxe;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.feature.Strength;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
public class PickaxeItem implements Item, Strength {

  private int strength = Sizes.DEFAULT_PICKAXE_STRENGTH;

  public PickaxeItem() { }

  public PickaxeItem(int strength) {
    this.strength = strength;
  }

  public int getStrength() {
    return strength;
  }

  @Override
  public Class<? extends ItemController> getItemController() {
    return PickaxeItemController.class;
  }

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(ImageRendererImpl.class, "pickaxeRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends EquippedItemRenderer<PickaxeItem>> getEquippedItemRenderer() {
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
