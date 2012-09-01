package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 01.09.12
 */
public interface LadderDefinition {

  public static int WIDTH = 2 * Sizes.BLOCK;
  public static int HEIGHT = 2 * Sizes.BLOCK;

  public static final BeanName<ImageRendererImpl> RENDERER = new BeanName<ImageRendererImpl>(ImageRendererImpl.class, "ladderRenderer");

}
