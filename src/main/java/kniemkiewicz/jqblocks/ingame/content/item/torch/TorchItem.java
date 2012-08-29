package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 29.08.12
 */
public class TorchItem implements Item {

  @Override
  public Class<? extends ItemController> getItemController() {
    return TorchItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return TorchDefinition.RENDERER;
  }

  @Override
  public BeanName<EmptyItemRenderer> getEquippedItemRenderer() {
    return EmptyItemRenderer.RENDERER;
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
