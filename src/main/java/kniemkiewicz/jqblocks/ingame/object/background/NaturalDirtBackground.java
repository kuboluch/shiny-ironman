package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/20/12
 */
public class NaturalDirtBackground implements RenderableObject{

  public static Color BROWN = new Color(100.0f/255, 50.0f/255, 0);
  Rectangle rectangle;
  int x;
  int y;
  int width;
  int height;

  NaturalDirtBackground(float x, float y, float width, float height) {
    this.x =  Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    this.width =  Sizes.roundToBlockSize(width);
    this.height = Sizes.roundToBlockSize(height);
    this.rectangle = new Rectangle(this.x, this.y, this.width, this.height);
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(BROWN);
    g.fillRect(x, y, width, height);
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  @Override
  public Layer getLayer() {
    return Layer.BACKGROUND;
  }
}
