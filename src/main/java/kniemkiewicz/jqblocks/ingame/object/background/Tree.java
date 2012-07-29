package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.AxeItem;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.PickaxeItem;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.resource.Wood;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/23/12
 */
public class Tree extends AbstractBackgroundElement<Tree> implements ResourceBackgroundElement<Wood> {

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
