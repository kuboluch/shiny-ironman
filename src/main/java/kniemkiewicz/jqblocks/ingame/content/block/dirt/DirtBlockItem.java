package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public class DirtBlockItem implements Item, UpdateQueue.ToBeUpdated<DirtBlockItem> {

  public Class<? extends DirtBlockItemController> getItemController() {
    return DirtBlockItemController.class;
  }

  private static final BeanName<DirtBlockItemRenderer> RENDERER = new BeanName<DirtBlockItemRenderer>(DirtBlockItemRenderer.class);

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
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<DirtBlockItem>> getUpdateController() {
    return DirtBlockItemController.class;
  }
}