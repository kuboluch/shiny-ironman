package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 15.08.12
 */
public class LadderItem implements Item {

  @Override
  public Class<? extends ItemController> getItemController() {
    return LadderItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return LadderDefinition.RENDERER;
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
