package kniemkiewicz.jqblocks.ingame.item;

import kniemkiewicz.jqblocks.ingame.Renderable;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public class EmptyItemRenderer implements ItemRenderer<Item>, Renderable {

  public static final BeanName<EmptyItemRenderer> RENDERER = new BeanName<EmptyItemRenderer>(EmptyItemRenderer.class);

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size, boolean drawFlipped) { }

  @Override
  public void render(Graphics g) {  }
}
