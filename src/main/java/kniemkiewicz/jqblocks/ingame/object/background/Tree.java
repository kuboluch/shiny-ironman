package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/23/12
 */
public class Tree implements RenderableObject {

  private Image image;

  Rectangle rectangle;
  int x;
  int y;
  public static int HEIGHT = Sizes.BLOCK * 4;
  public static int WIDTH = Sizes.BLOCK * 2;

  Tree(int x, int y, Image image) {
    this.x = Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    rectangle = new Rectangle(x, y, WIDTH, HEIGHT);
    this.image = image;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    image.draw(x, y, WIDTH, HEIGHT);
  }

  @Override
  public Layer getLayer() {
    return Layer.BACKGROUND;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }
}
