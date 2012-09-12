package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.renderer.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public class EmptyItem implements Item {

  public Class<? extends ItemController> getItemController() {
    return null;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return EmptyItemRenderer.RENDERER;
  }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public BeanName<? extends EquippedItemRenderer> getEquippedItemRenderer() {
    return null;
  }
}
