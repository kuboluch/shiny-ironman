package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public abstract class AbstractBackgroundElement implements BackgroundElement {
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected Rectangle shape;

  public AbstractBackgroundElement(int x, int y, int width, int height) {
    this.x =  Sizes.floorToBlockSizeX(x);
    this.y = Sizes.floorToBlockSizeY(y);
    this.width =  Sizes.floorToBlockSize(width);
    this.height = Sizes.floorToBlockSize(height);
    shape = new Rectangle(this.x, this.y, this.width, this.height);
    assert this.width > 0;
    assert this.height > 0;
  }

  @Override
  public boolean isResource() {
    return false;
  }

  @Override
  public boolean isWorkplace() {
    return false;
  }

  @Override
  abstract public BeanName<? extends ObjectRenderer> getRenderer();

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.BACKGROUND;
  }

  @Override
  public Rectangle getShape() {
    return shape;
  }
}
