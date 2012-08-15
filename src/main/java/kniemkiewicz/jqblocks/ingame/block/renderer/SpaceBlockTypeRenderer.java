package kniemkiewicz.jqblocks.ingame.block.renderer;

import kniemkiewicz.jqblocks.ingame.block.RenderableBlockType;
import kniemkiewicz.jqblocks.ingame.block.WallBlockType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.XMLPackedSheet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 12.08.12
 */
@Component
public class SpaceBlockTypeRenderer implements RenderableBlockType.Renderer<WallBlockType> {

  @Resource(name = "blockSheet")
  XMLPackedSheet blockSheet;

  @Override
  public void renderBlock(int x, int y, int width, int height, Graphics g) {
  }

  @Override
  public void renderBorder(int x, int y, int length, RenderableBlockType.Border type, WallBlockType other, Graphics g) {
  }
}
