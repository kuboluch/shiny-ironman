package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.controller.AxeItemController;
import kniemkiewicz.jqblocks.ingame.item.controller.PickaxeItemController;
import kniemkiewicz.jqblocks.ingame.item.feature.Strength;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class AxeItem implements Item, Strength, UpdateQueue.ToBeUpdated<AxeItem> {

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
  public Class<AxeItemController> getUpdateController() {
    return AxeItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(ImageItemRenderer.class, "axeRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public void renderItem(Graphics g, int x, int y, int square_size) {  }
}
