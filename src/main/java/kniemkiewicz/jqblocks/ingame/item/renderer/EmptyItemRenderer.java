package kniemkiewicz.jqblocks.ingame.item.renderer;

import kniemkiewicz.jqblocks.ingame.item.Item;
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
  public void renderItem(Item item, int x, int y, int square_size, boolean drawFlipped) {
  }

  @Override
  public void renderEquippedItem(Item item, Graphics g) {
  }
}
