package kniemkiewicz.jqblocks.ingame.block;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 12.08.12
 */
@Component
public class SpaceBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) { }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, WallBlockType other, Graphics g) { }
}
