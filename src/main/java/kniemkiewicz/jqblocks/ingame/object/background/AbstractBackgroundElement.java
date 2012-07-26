package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public abstract class AbstractBackgroundElement<T extends AbstractBackgroundElement>
    implements BackgroundElement, RenderableObject<T> {
  int x;
  int y;
  int width;
  int height;
  protected Rectangle shape;

  protected AbstractBackgroundElement(int x, int y, int width, int height) {
   this.x =  Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    this.width =  Sizes.roundToBlockSize(width);
    this.height = Sizes.roundToBlockSize(height);
    shape = new Rectangle(this.x, this.y, this.width, this.height);
    assert this.width > 0;
    assert this.height > 0;
  }

  @Override
  public boolean isResource() {
    return false;
  }

  @Override
  public Class<? extends ObjectRenderer<T>> getRenderer() {
    return null;
  }

  @Override
  public abstract void renderObject(Graphics g, PointOfView pov);

  @Override
  public Layer getLayer() {
    return Layer.BACKGROUND;
  }

  @Override
  public Rectangle getShape() {
    return shape;
  }
}
