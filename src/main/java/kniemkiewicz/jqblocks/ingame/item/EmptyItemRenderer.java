package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class EmptyItemRenderer implements ItemRenderer<Item>, EquippedItemRenderer<Item> {

  public static final BeanName<EmptyItemRenderer> RENDERER = new BeanName<EmptyItemRenderer>(EmptyItemRenderer.class);

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size, boolean drawFlipped) {
  }

  @Override
  public void renderEquippedItem(Item item, Graphics g) {
  }

  @Override
  public void resetEquippedItemRenderer() {
  }

  @Override
  public Image getImage(Item item) {
    return null;
  }
}
