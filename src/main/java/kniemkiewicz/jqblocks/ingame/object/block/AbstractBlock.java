package kniemkiewicz.jqblocks.ingame.object.block;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.SolidBlocks;
import kniemkiewicz.jqblocks.ingame.object.NeighborAwareObject;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

/**
 * User: krzysiek
 * Date: 14.07.12
 */
public abstract class AbstractBlock<T extends AbstractBlock> implements RenderableObject<T>, PhysicalObject, NeighborAwareObject<AbstractBlock>, Serializable {
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected Rectangle shape;
  transient protected NeighborAwareObject neighbors;
  protected int endurance = Sizes.DEFAULT_BLOCK_ENDURANCE;

  public AbstractBlock(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
    neighbors = new NeighborAwareBlock();
    assert this.width > 0;
    assert this.height > 0;
  }

  public AbstractBlock(float x, float y, float width, float height) {
    this.x = Sizes.roundToBlockSizeX(x);
    this.y = Sizes.roundToBlockSizeY(y);
    this.width = Sizes.roundToBlockSize(width);
    this.height = Sizes.roundToBlockSize(height);
    shape = new Rectangle(this.x, this.y, this.width - 1, this.height - 1);
    neighbors = new NeighborAwareBlock();
    assert this.width > 0;
    assert this.height > 0;
  }

  protected abstract AbstractBlock getSubBlock(AbstractBlock parent, int x, int y, int width, int height);

  public abstract void renderObject(Graphics g, PointOfView pov);

  @Override
  public BeanName<? extends ObjectRenderer<T>> getRenderer() {
    return null;
  }

  public Rectangle getShape() {
    return shape;
  }

  // Returns a rectangle that was actually removed or null if block remains unchanged.
  public Rectangle removeRect(Rectangle rect, SolidBlocks blocks) {
    int rectMinY = Math.max(Sizes.roundToBlockSizeY(rect.getMinY()), y);
    int rectMaxY = Math.min(Sizes.roundToBlockSizeY(rect.getMaxY()), y + height);
    int rectMinX = Math.max(Sizes.roundToBlockSizeX(rect.getMinX()), x);
    int rectMaxX = Math.min(Sizes.roundToBlockSizeX(rect.getMaxX()), x + width);
    if ((rectMinY == rectMaxY) || (rectMinX == rectMaxX)) return null;
    blocks.remove(this);
    // We have to check this after removing the block itself.
    assert !blocks.intersects(this.getShape()).hasNext();
    if (y < rectMinY) {
      Assert.executeAndAssert(blocks.add(getSubBlock(this, x, y, width, rectMinY - y)));
    }
    if (x < rectMinX) {
      Assert.executeAndAssert(blocks.add(getSubBlock(this, x, rectMinY, rectMinX - x, rectMaxY - rectMinY)));
    }
    if (x + width > rectMaxX) {
      Assert.executeAndAssert(blocks.add(getSubBlock(this, rectMaxX, rectMinY, x + width - rectMaxX, rectMaxY - rectMinY)));
    }
    if (y + height > rectMaxY) {
      Assert.executeAndAssert(blocks.add(getSubBlock(this, x, rectMaxY, width, y + height - rectMaxY)));
    }
    return new Rectangle(rectMinX, rectMinY, rectMaxX - rectMinX, rectMaxY - rectMinY);
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
  public void removeLeftNeighbor(AbstractBlock neighbor) {
    neighbors.removeLeftNeighbor(neighbor);
  }

  @Override
  public void removeTopNeighbor(AbstractBlock neighbor) {
    neighbors.removeTopNeighbor(neighbor);
  }

  @Override
  public void removeRightNeighbor(AbstractBlock neighbor) {
    neighbors.removeRightNeighbor(neighbor);
  }

  @Override
  public void removeBottomNeighbor(AbstractBlock neighbor) {
    neighbors.removeBottomNeighbor(neighbor);
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

  public int getEndurance() {
    return endurance;
  }

  public void setEndurance(int endurance) {
    this.endurance = endurance;
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

  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    // Hope this enough. Without this we would get NPE.
    neighbors = new NeighborAwareBlock();
  }
}
