package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 20.09.12
 */
public class SpriteSheetBlockTypeRenderer<T extends RenderableBlockType> implements RenderableBlockType.Renderer<T> {

  boolean renderBorder = false;
  final String spriteName;
  XMLPackedSheet blockSheet;
  final T blockType;

  public SpriteSheetBlockTypeRenderer(String spriteName, XMLPackedSheet blockSheet, T blockType) {
    this.spriteName = spriteName;
    this.blockSheet = blockSheet;
    this.blockType = blockType;
  }

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
    texture(x, y, width, height, g, blockSheet.getSprite(spriteName));
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, T other, Graphics g) {
    if (!renderBorder) return;
    if (other == blockType) return;
    g.setColor(Color.black);
    switch (type) {
      case TOP:
        g.drawLine(x, y + 1, x + length - 1, y - 1);
        break;
      case BOTTOM:
        g.drawLine(x, y, x + length - 1, y + 1);
        break;
      case LEFT:
        g.drawLine(x - 1, y, x - 1, y + length);
        break;
      case RIGHT:
        g.drawLine(x - 1, y, x - 1, y + length);
        break;
    }
  }

  protected void texture(int x, int y, int width, int height, Graphics g, Image texture) {
    int stepX = texture.getWidth();
    int drawX;
    for (drawX = x; drawX + stepX < (x + width); drawX += stepX) {
      drawColumn(y, height, g, texture, drawX, stepX);
    }
    int diffX = x + width - drawX;
    if (diffX > 0) {
      drawColumn(y, height, g, texture, drawX, diffX);
    }
  }

  private void drawColumn(int y, int height, Graphics g, Image texture, int drawX,int stepX) {
    int stepY = texture.getHeight();
    int drawY;
    for (drawY = y; drawY + stepY < (y + height); drawY += stepY) {
      g.drawImage(texture, drawX, drawY, drawX + stepX, drawY + stepY, 0, 0, stepX, stepY);
    }
    int diffY = y + height - drawY;
    if (diffY > 0) {
      g.drawImage(texture, drawX, drawY, drawX + stepX, drawY + diffY, 0, 0, stepX, diffY);
    }
  }

  public void setRenderBorder(boolean renderBorder) {
    this.renderBorder = renderBorder;
  }
}
