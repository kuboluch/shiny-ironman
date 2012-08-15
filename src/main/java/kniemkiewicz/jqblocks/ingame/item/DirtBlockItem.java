package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.item.controller.DirtBlockItemController;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 11.07.12
 */
public class DirtBlockItem implements Item, UpdateQueue.ToBeUpdated<DirtBlockItem> {
  public void renderItem(Graphics g, int x, int y, int square_size) {
    x+= square_size / 10;
    y+= square_size / 10;
    square_size = square_size * 9 / 10;
    g.setColor(DirtBlockTypeRenderer.BROWN);
    g.fillRoundRect(x, y, square_size, square_size, 5);
    g.setColor(DirtBlockTypeRenderer.DARK_GREEN);
    g.drawRoundRect(x, y, square_size, square_size, 5);
    g.drawRoundRect(x +1, y + 1, square_size - 2, square_size - 2, 5);
  }

  public Class<? extends DirtBlockItemController> getItemController() {
    return DirtBlockItemController.class;
  }

  @Override
  public BeanName<? extends ItemRenderer> getItemRenderer() {
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
