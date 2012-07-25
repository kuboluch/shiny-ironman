package kniemkiewicz.jqblocks.ingame.object.block;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public class EndOfTheWorldWall extends AbstractBlock {
  public EndOfTheWorldWall(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  @Override
  protected AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height) {
    throw new IllegalStateException();
  }
  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(Color.black);
    g.fillRect(x,y,width,height);
  }

  @Override
  public Layer getLayer() {
    return Layer.WALL;
  }
}
