package kniemkiewicz.jqblocks.ingame.object.block;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 8/3/12
 */
public class RockBlock extends AbstractBlock<RockBlock> {
  public RockBlock(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public RockBlock(float x, float y, float width, float height) {
    super(x, y, width, height);
  }

  @Override
  protected AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height) {
    return new RockBlock(x, y, width, height);
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(Color.gray);
    g.fillRect(x, y, width, height);
    g.setColor(Color.black);
    g.drawRect(x, y, width, height);
  }

  @Override
  public Layer getLayer() {
    return Layer.WALL;
  }
}
