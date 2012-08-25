package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: qba
 * Date: 15.08.12
 */
public class LadderItem implements Item, UpdateQueue.ToBeUpdated<LadderItem> {

  @Override
  public Class<? extends ItemController> getItemController() {
    return LadderItemController.class;
  }

  private static final BeanName<ItemRenderer> RENDERER = new BeanName<ItemRenderer>(ImageRenderer.class, "ladderRenderer");

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
  public Class<? extends UpdateQueue.UpdateController<LadderItem>> getUpdateController() {
    return LadderItemController.class;
  }
}
