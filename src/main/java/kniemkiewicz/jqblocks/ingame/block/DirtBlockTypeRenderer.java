package kniemkiewicz.jqblocks.ingame.block;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 07.08.12
 */
@Component
public class DirtBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  private static int LINE_WIDTH = 2;
  public static Color BROWN = new Color(150.0f/255, 75.0f/255, 0);
  public static Color DARK_GREEN = new Color(0, 0.75f, 0);

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    g.setColor(BROWN);
    g.fillRect(x, y + LINE_WIDTH, width, height - LINE_WIDTH);
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, WallBlockType other, Graphics g) {
    if (other == WallBlockType.DIRT) return;
    g.setColor(DARK_GREEN);
    g.setLineWidth(LINE_WIDTH);
    switch (type) {
      case TOP:
      case BOTTOM:
        g.drawLine(x, y, x + length, y);
        break;
      case LEFT:
      case RIGHT:
        g.drawLine(x, y, x, y + length);
        break;
    }
    g.setLineWidth(1);
  }
}
