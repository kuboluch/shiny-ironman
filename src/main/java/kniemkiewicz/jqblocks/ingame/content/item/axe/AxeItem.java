package kniemkiewicz.jqblocks.ingame.content.item.axe;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.feature.Strength;
import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

public class AxeItem implements Item, Strength {

  private int strength = Sizes.DEFAULT_AXE_STRENGTH;

  public AxeItem() { }

  public AxeItem(int strength) {
    this.strength = strength;
  }

  public int getStrength() {
    return strength;
  }
  @Override
  public Class<? extends ItemController> getItemController() {
    return AxeItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(SimpleImageRenderer.class, "axeRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends EquippedItemRenderer<AxeItem>> getEquippedItemRenderer() {
    return null;
  }
}
