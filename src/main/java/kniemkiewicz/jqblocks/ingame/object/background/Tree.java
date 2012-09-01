package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.content.resource.Wood;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.item.axe.AxeItem;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/23/12
 */
public class Tree extends AbstractBackgroundElement<Tree> implements ResourceBackgroundElement<Wood> {

  private static final long serialVersionUID = 1;

  public static int HEIGHT = Sizes.BLOCK * 8;
  public static int WIDTH = Sizes.BLOCK * 4;

  public Tree(int x, int y) {
    super(x, y, WIDTH, HEIGHT);
  }

  @Override
  public boolean isResource() {
    return true;
  }

  @Override
  public boolean requiresFoundation() {
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRendererImpl.class, "treeRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public Class getMiningItemType() {
    return AxeItem.class;
  }

  @Override
  public Wood mine(int delta) {
    return new Wood(delta * Sizes.DEFAULT_RESOURCE_RICHNESS);
  }
}
