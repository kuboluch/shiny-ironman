package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.Backgrounds;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public abstract class AbstractBlock implements RenderableObject, PhysicalObject {
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected Rectangle shape;

  public AbstractBlock(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
  }

  public AbstractBlock(float x, float y, float width, float height) {
    this.x =  Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    this.width =  Sizes.roundToBlockSize(width);
    this.height = Sizes.roundToBlockSize(height);
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
  }

  protected abstract AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height);

  public abstract void renderObject(Graphics g, PointOfView pov);

  public Rectangle getShape() {
    return shape;
  }

  public void removeRect(Rectangle rect, SolidBlocks blocks, Backgrounds backgrounds) {
    blocks.remove(this);
    // We have to check this after removing the block itself.
    Assert.assertThat(!blocks.intersects(this.getShape()).hasNext());
    int rectMinY = Sizes.roundToBlockSizeY(rect.getMinY());
    int rectMaxY = Sizes.roundToBlockSizeY(rect.getMaxY());
    int rectMinX = Sizes.roundToBlockSizeX(rect.getMinX());
    int rectMaxX = Sizes.roundToBlockSizeX(rect.getMaxX());
    if (y < rectMinY) {
      blocks.add(getSubBlock(this, x, y, width, rectMinY - y));
    }
    if (x < rectMinX) {
      blocks.add(getSubBlock(this, x, rectMinY, rectMinX - x, rectMaxY - rectMinY));
    }
    if (x + width > rectMaxX) {
      blocks.add(getSubBlock(this, rectMaxX, rectMinY, x + width - rectMaxX, rectMaxY - rectMinY));
    }
    if (y + height > rectMaxY) {
      blocks.add(getSubBlock(this, x, rectMaxY, width, y + height - rectMaxY));
    }
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" +
        "x=" + x +
        ", y=" + y +
        ", width=" + width +
        ", height=" + height +
        '}';
  }
}
