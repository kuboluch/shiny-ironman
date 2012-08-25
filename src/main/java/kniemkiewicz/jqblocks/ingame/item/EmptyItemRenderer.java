package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class EmptyItemRenderer implements ItemRenderer<Item> {

  public static final BeanName<EmptyItemRenderer> RENDERER = new BeanName<EmptyItemRenderer>(EmptyItemRenderer.class);

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size) { }
}
