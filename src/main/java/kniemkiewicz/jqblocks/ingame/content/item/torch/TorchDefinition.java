package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 29.08.12
 */
public interface TorchDefinition {

  public static int WIDTH = Sizes.BLOCK;
  public static int HEIGHT = Sizes.BLOCK;

  public static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "torchRenderer");

}
