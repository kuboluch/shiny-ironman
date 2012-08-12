package kniemkiewicz.jqblocks.ingame.block;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 12.08.12
 */
@Component
public class RockBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    g.setColor(Color.gray);
    g.fillRect(x, y, width, height + 1);
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, WallBlockType other, Graphics g) {
    if (other == WallBlockType.ROCK) return;
    g.setColor(Color.black);
    switch (type) {
      case TOP:
        g.drawLine(x, y - 1, x + length - 1, y - 1);
        break;
      case BOTTOM:
        g.drawLine(x, y + 1, x + length - 1, y + 1);
        break;
      case LEFT:
        g.drawLine(x - 1, y, x - 1, y + length);
        break;
      case RIGHT:
        g.drawLine(x - 1, y, x - 1, y + length);
        break;
    }
  }
}
