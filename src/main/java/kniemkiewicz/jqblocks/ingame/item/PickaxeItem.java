package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.controller.PickaxeItemController;
import kniemkiewicz.jqblocks.ingame.item.feature.Strength;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

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

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(ImageItemRenderer.class, "pickaxeRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public void renderItem(Graphics g, int x, int y, int square_size) {  }

  @Override
  public Class<PickaxeItemController> getUpdateController() {
    return PickaxeItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }
}
