package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 01.09.12
 */
public class DirtBlockDefinition {

  public static final BeanName<ImageRendererImpl> RENDERER = new BeanName<ImageRendererImpl>(ImageRendererImpl.class, "dirtBlockRenderer");
}
