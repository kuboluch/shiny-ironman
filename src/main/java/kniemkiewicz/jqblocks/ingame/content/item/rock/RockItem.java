package kniemkiewicz.jqblocks.ingame.content.item.rock;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/25/12
 */
public class RockItem implements Item {

  boolean large;

  public RockItem(boolean large) {
    this.large = large;
  }

  @Override
  public Class<? extends ItemController> getItemController() {
    return RockItemController.class;
  }

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(RockRenderer.class);

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return RENDERER;
  }

  @Override
  public BeanName<? extends Renderable> getEquippedItemRenderer() {
    return null;
  }

  @Override
  public boolean isLarge() {
    return large;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
