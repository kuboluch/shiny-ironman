package kniemkiewicz.jqblocks.ingame.content.item.pickaxe;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.feature.Strength;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
public class PickaxeItem implements Item, Strength, UpdateQueue.ToBeUpdated<PickaxeItem> {

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

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(ImageRenderer.class, "pickaxeRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends Renderable> getEquippedItemRenderer() {
    return null;
  }

  @Override
  public Class<PickaxeItemController> getUpdateController() {
    return PickaxeItemController.class;
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
