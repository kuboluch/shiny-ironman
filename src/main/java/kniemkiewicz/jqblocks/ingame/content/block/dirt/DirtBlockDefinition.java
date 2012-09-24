package kniemkiewicz.jqblocks.ingame.content.block.dirt;

import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 01.09.12
 */
public class DirtBlockDefinition {

  public static final BeanName<SimpleImageRenderer> RENDERER = new BeanName<SimpleImageRenderer>(SimpleImageRenderer.class, "dirtBlockRenderer");
}
