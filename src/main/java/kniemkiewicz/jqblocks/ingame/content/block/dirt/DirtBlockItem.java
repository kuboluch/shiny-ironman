package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EmptyItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.inventory.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public class DirtBlockItem implements Item {

  public Class<? extends DirtBlockItemController> getItemController() {
    return DirtBlockItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
    return DirtBlockDefinition.RENDERER;
  }

  @Override
  public BeanName<? extends EquippedItemRenderer> getEquippedItemRenderer() {
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
