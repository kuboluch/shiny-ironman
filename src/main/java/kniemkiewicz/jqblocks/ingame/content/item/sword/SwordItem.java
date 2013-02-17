package kniemkiewicz.jqblocks.ingame.content.item.sword;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

public class SwordItem implements Item {

  public static int DAMAGE = 40;

  double arc = 0;
  boolean moving = false;

  @Override
  public Class<? extends ItemController> getItemController() {
    return SwordItemController.class;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  private static final BeanName<ItemRenderer> SIMPLE_RENDERER = BeanName.<ItemRenderer>of(SimpleImageRenderer.class, "swordRenderer");

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return SIMPLE_RENDERER;
  }

  private static final BeanName<SwordRenderer> SWORD_RENDERER = BeanName.of(SwordRenderer.class);

  @Override
  public BeanName<? extends EquippedItemRenderer<SwordItem>> getEquippedItemRenderer() {
    return SWORD_RENDERER;
  }

  public double getArc() {
    return arc;
  }

  public void setArc(double arc) {
    this.arc = arc;
  }

  public boolean isMoving() {
    return moving;
  }

  public void setMoving(boolean moving) {
    this.moving = moving;
  }
}
