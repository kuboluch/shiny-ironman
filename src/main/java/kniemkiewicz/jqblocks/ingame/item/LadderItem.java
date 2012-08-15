package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.controller.ItemController;
import kniemkiewicz.jqblocks.ingame.object.transport.LadderItemController;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: qba
 * Date: 15.08.12
 */
public class LadderItem implements Item {

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
  public void renderItem(Graphics g, int x, int y, int square_size) { }

  @Override
  public boolean isLarge() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
