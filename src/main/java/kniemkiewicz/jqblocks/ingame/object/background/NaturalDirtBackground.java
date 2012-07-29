package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/20/12
 */
public class NaturalDirtBackground extends AbstractBackgroundElement<NaturalDirtBackground> {

  public static Color BROWN = new Color(100.0f/255, 50.0f/255, 0);

  NaturalDirtBackground(float x, float y, float width, float height) {
    super(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y),
        Sizes.roundToBlockSize(width), Sizes.roundToBlockSize(height));
  }

  @Override
  public Class<? extends ObjectRenderer<NaturalDirtBackground>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(BROWN);
    g.fillRect(x, y, width, height);
  }
}
