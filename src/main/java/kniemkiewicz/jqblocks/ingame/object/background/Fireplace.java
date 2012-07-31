package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/30/12
 */
public class Fireplace extends AbstractBackgroundElement<Fireplace>{

  public static final int WIDTH = Sizes.BLOCK * 4;
  public static final int HEIGHT = Sizes.BLOCK * 3;

  public Fireplace(int x, int y) {
    super(x, y, WIDTH, HEIGHT);
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "fireplaceRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super Fireplace>> getRenderer() {
    return RENDERER;
  }

  @Override
  public Layer getLayer() {
    return Layer.PASSIVE_OBJECTS;
  }
}
