package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.Backgrounds;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public abstract class AbstractBlock implements RenderableObject, PhysicalObject, NeighborAwareObject<AbstractBlock> {
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected Rectangle shape;
  protected NeighborAwareObject neighbors;

  public AbstractBlock(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
    neighbors = new NeighborAwareBlock();
  }

  public AbstractBlock(float x, float y, float width, float height) {
    this.x = Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    this.width = Sizes.roundToBlockSize(width);
    this.height = Sizes.roundToBlockSize(height);
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
    neighbors = new NeighborAwareBlock();
  }

  protected abstract AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height);

  public abstract void renderObject(Graphics g, PointOfView pov);

  public Rectangle getShape() {
    return shape;
  }

  public void removeRect(Rectangle rect, SolidBlocks blocks, Backgrounds backgrounds) {
    blocks.remove(this);
    // We have to check this after removing the block itself.
    assert !blocks.intersects(this.getShape()).hasNext();
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
  public List<AbstractBlock> getLeftNeighbors() {
    return neighbors.getLeftNeighbors();
  }

  @Override
  public List<AbstractBlock> getTopNeighbors() {
    return neighbors.getTopNeighbors();
  }

  @Override
  public List<AbstractBlock> getRightNeighbors() {
    return neighbors.getRightNeighbors();
  }

  @Override
  public List<AbstractBlock> getBottomNeighbors() {
    return neighbors.getBottomNeighbors();
  }

  @Override
  public void addLeftNeighbor(AbstractBlock neighbor) {
    neighbors.addLeftNeighbor(neighbor);
  }

  @Override
  public void addTopNeighbor(AbstractBlock neighbor) {
    neighbors.addTopNeighbor(neighbor);
  }

  @Override
  public void addRightNeighbor(AbstractBlock neighbor) {
    neighbors.addRightNeighbor(neighbor);
  }

  @Override
  public void addBottomNeighbor(AbstractBlock neighbor) {
    neighbors.addBottomNeighbor(neighbor);
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
