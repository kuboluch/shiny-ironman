package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 07.08.12
 */
@Component
public class DirtBlockTypeRenderer extends SpriteSheetBlockTypeRenderer<WallBlockType> {

  private static int LINE_WIDTH = 2;
  public static Color BROWN = new Color(150.0f/255, 75.0f/255, 0);
  public static Color DARK_GREEN = new Color(84f/255, 129f/255, 73f/255);

  @Resource( name="blockSheet" )
  XMLPackedSheet autowiredBlockSheet;

  @Autowired
  public DirtBlockTypeRenderer(XMLPackedSheet blockSheet) {
    super("dirt", blockSheet, WallBlockType.DIRT);
  }

  @PostConstruct
  void init() {
    this.blockSheet = autowiredBlockSheet;
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
}
