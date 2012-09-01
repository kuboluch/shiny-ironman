package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/23/12
 */
public class LadderBackground extends AbstractBackgroundElement<LadderBackground> {

  private static final long serialVersionUID = 1;

  public LadderBackground(int x, int y) {
    super(x, y, LadderDefinition.WIDTH, LadderDefinition.HEIGHT);
  }

  @Override
  public boolean isResource() {
    return true;
  }

  @Override
  public boolean requiresFoundation() {
    return false;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRendererImpl.class, "ladderRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }
}
