package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/28/12
 */
@Component
public class TreeRenderer implements ObjectRenderer<Tree> {

  @Resource(name = "treeImage")
  Image image;

  @Override
  public void render(Tree object, Graphics g, PointOfView pov) {
    image.draw(object.x, object.y, Tree.WIDTH, Tree.HEIGHT);
  }
}
