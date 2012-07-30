package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.AxeItem;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.resource.Wood;

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

  @Override
  public Class<? extends ObjectRenderer<Tree>> getRenderer() {
    return TreeRenderer.class;
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
