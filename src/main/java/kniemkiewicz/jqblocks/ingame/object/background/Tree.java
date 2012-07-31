package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.AxeItem;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.resource.Wood;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: knie
 * Date: 7/23/12
 */
public class Tree extends AbstractBackgroundElement<Tree> implements ResourceBackgroundElement<Wood> {

  private static final long serialVersionUID = 1;

  public static int HEIGHT = Sizes.BLOCK * 8;
  public static int WIDTH = Sizes.BLOCK * 4;

  Tree(int x, int y) {
    super(x, y, WIDTH, HEIGHT);
  }

  @Override
  public boolean isResource() {
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "treeRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super Tree>> getRenderer() {
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
