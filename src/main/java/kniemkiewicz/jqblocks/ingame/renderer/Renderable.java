package kniemkiewicz.jqblocks.ingame.renderer;

import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 10.07.12
 */
public interface Renderable {
  void render(Graphics g);
  boolean isDisposable();
}
