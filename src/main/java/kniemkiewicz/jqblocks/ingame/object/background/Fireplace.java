package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageItemRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.bat.BatRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/30/12
 */
public class Fireplace extends AbstractBackgroundElement<Fireplace>{

  public static final int WIDTH = Sizes.BLOCK * 3;
  public static final int HEIGHT = Sizes.BLOCK * 2;

  protected Fireplace(int x, int y) {
    super(x, y, WIDTH, HEIGHT);
  }

  private static final BeanName<ImageItemRenderer> RENDERER = new BeanName<ImageItemRenderer>(ImageItemRenderer.class, "fireplaceRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super Fireplace>> getRenderer() {
    return RENDERER;
  }
}
