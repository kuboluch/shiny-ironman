package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.block.BackgroundBlockType;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 20.09.12
 */
@Component
public class NaturalDirtBackgroundRenderer implements RenderableBlockType.Renderer<BackgroundBlockType>{

  public static Color BROWN = new Color(100.0f/255, 50.0f/255, 0);

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    g.setColor(BROWN);
    g.fillRect(x, y, width, height);
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, BackgroundBlockType other, Graphics g) { }
}
