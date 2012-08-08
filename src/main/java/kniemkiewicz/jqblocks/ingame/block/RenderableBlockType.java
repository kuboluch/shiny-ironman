package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 07.08.12
 */
public interface RenderableBlockType {
  enum Border {
    TOP,BOTTOM,LEFT,RIGHT
  }
  public interface Renderer<T extends RenderableBlockType> {
    void renderBlock(int x, int y, int width, int height, Graphics g);

    void renderBorder(int x, int y, int length, Border type, T other, Graphics g);
  }

  BeanName<? extends Renderer> getRenderer();

  RenderableObject.Layer getLayer();
}
