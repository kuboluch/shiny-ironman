package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * User: knie
 * Date: 7/20/12
 */
public class NaturalDirtBackground extends AbstractBackgroundElement<NaturalDirtBackground> {

  private static final long serialVersionUID = 1;

  public static Color BROWN = new Color(100.0f/255, 50.0f/255, 0);

  public NaturalDirtBackground(float x, float y, float width, float height) {
    super(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y),
        Sizes.roundToBlockSize(width), Sizes.roundToBlockSize(height));
  }

  @Override
  public BeanName<? extends ObjectRenderer<NaturalDirtBackground>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(BROWN);
    g.fillRect(x, y, width, height);
  }

  @Override
  public boolean requiresFoundation() {
    return false;
  }
}
