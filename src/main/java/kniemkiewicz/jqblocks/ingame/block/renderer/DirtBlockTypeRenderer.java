package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 07.08.12
 */
@Component
public class DirtBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  private static int LINE_WIDTH = 2;
  public static Color BROWN = new Color(150.0f/255, 75.0f/255, 0);
  public static Color DARK_GREEN = new Color(84f/255, 129f/255, 73f/255);

  @Resource( name="blockSheet" )
  XMLPackedSheet blockSheet;

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    texture(x, y, width, height, g, blockSheet.getSprite("dirt"));
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, WallBlockType other, Graphics g) {
    if (other != WallBlockType.EMPTY) return;
    g.setColor(DARK_GREEN);
    g.setLineWidth(LINE_WIDTH);
    switch (type) {
      case TOP:
        texture(x, y, length, Sizes.BLOCK, g, blockSheet.getSprite("dirtWithGrass"));
        texture(x, y - Sizes.BLOCK, length, Sizes.BLOCK, g, blockSheet.getSprite("grass4"));
        break;
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

  private void texture(int x, int y, int width, int height, Graphics g, Image texture) {
    for (int drawX = x; drawX < (x + width); drawX += Sizes.BLOCK) {
      for (int drawY = y; drawY < (y + height); drawY += Sizes.BLOCK) {
        g.drawImage(texture, drawX, drawY, drawX + Sizes.BLOCK, drawY + Sizes.BLOCK, 0, 0, Sizes.BLOCK, Sizes.BLOCK);
      }
    }
  }
}
