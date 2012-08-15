package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 12.08.12
 */
@Component
public class RockBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  @Resource( name="blockSheet" )
  XMLPackedSheet blockSheet;

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    texture(x, y, width, height, g, blockSheet.getSprite("stone"));
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

  private void texture(int x, int y, int width, int height, Graphics g, Image texture) {
    for (int drawX = x; drawX < (x + width); drawX += Sizes.BLOCK) {
      for (int drawY = y; drawY < (y + height); drawY += Sizes.BLOCK) {
        g.drawImage(texture, drawX, drawY, drawX + Sizes.BLOCK, drawY + Sizes.BLOCK, 0, 0, Sizes.BLOCK, Sizes.BLOCK);
      }
    }
  }
}
