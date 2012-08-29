package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/23/12
 */
public class LadderBackground extends AbstractBackgroundElement<LadderBackground> {

  private static final long serialVersionUID = 1;

  public static int HEIGHT = 2 * Sizes.BLOCK;
  public static int WIDTH = 2 * Sizes.BLOCK;

  public LadderBackground(int x, int y) {
    super(x, y, WIDTH, HEIGHT);
  }

  @Override
  public boolean isResource() {
    return true;
  }

  @Override
  public boolean requiresFoundation() {
    return false;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "ladderRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super LadderBackground>> getRenderer() {
    return RENDERER;
  }
}
