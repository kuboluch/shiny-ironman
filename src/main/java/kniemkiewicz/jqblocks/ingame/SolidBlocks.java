package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.block.EndOfTheWorldWall;
import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.Collections3;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class SolidBlocks {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  CollisionController collisionController;

  @Autowired
  TimingInfo timingInfo;

  static final EnumSet<CollisionController.ObjectType> BLOCK_OBJECT_TYPE = EnumSet.of(CollisionController.ObjectType.WALL);

  @PostConstruct
  void init() {
    List<AbstractBlock> blocks = new ArrayList<AbstractBlock>();
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MAX_Y, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    blocks.add(new EndOfTheWorldWall(Sizes.MAX_X, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    for (AbstractBlock b : blocks) {
      renderQueue.add(b);
      Assert.executeAndAssert(collisionController.add(BLOCK_OBJECT_TYPE, b, true));
    }
  }

  public IterableIterator<AbstractBlock> intersects(Rectangle rect) {
    TimingInfo.Timer t = timingInfo.getTimer("SolidBlocks.intersects");
    IterableIterator<AbstractBlock> it = Collections3.getIterable(collisionController.<AbstractBlock>fullSearch(BLOCK_OBJECT_TYPE, rect).iterator());
    t.record();
    return it;
  }

  public boolean add(AbstractBlock block) {
    if (intersects(block.getShape()).hasNext()) return false;
    if (movingObjects.intersects(block.getShape()).hasNext()) return false;
    updateNeighbors(block);
    Assert.executeAndAssert(collisionController.add(BLOCK_OBJECT_TYPE, block, true));
    renderQueue.add(block);
    return true;
  }

  private void updateNeighbors(AbstractBlock block) {
    updateLeftNeighbors(block);
    updateTopNeighbors(block);
    updateRightNeighbors(block);
    updateBottomNeighbors(block);
  }

  private void updateLeftNeighbors(AbstractBlock block) {
    Rectangle leftNeighborTest = new Rectangle(block.getShape().getX() - 1, block.getShape().getY(), 1, block.getShape().getHeight());
    Iterator<AbstractBlock> iter = intersects(leftNeighborTest);
    while (iter.hasNext()) {
      AbstractBlock neighbor = iter.next();
      block.addLeftNeighbor(neighbor);
      if (!neighbor.getRightNeighbors().contains(block)) {
        neighbor.addRightNeighbor(block);
      }
    }
  }

  private void updateTopNeighbors(AbstractBlock block) {
    Rectangle topNeighborTest = new Rectangle(block.getShape().getX(), block.getShape().getY() - 1, block.getShape().getWidth(), 1);
    Iterator<AbstractBlock> iter = intersects(topNeighborTest);
    while (iter.hasNext()) {
      AbstractBlock neighbor = iter.next();
      block.addTopNeighbor(neighbor);
      if (!neighbor.getBottomNeighbors().contains(block)) {
        neighbor.addBottomNeighbor(block);
      }
    }
  }

  private void updateRightNeighbors(AbstractBlock block) {
    Rectangle rightNeighborTest = new Rectangle(block.getShape().getX() + block.getShape().getWidth(), block.getShape().getY(), 1, block.getShape().getHeight());
    Iterator<AbstractBlock> iter = intersects(rightNeighborTest);
    while (iter.hasNext()) {
      AbstractBlock neighbor = iter.next();
      block.addRightNeighbor(neighbor);
      if (!neighbor.getLeftNeighbors().contains(block)) {
        neighbor.addLeftNeighbor(block);
      }
    }
  }

  private void updateBottomNeighbors(AbstractBlock block) {
    Rectangle bottomNeighborTest = new Rectangle(block.getShape().getX(), block.getShape().getY() + block.getShape().getHeight(), block.getShape().getWidth(), 1);
    Iterator<AbstractBlock> iter = intersects(bottomNeighborTest);
    while (iter.hasNext()) {
      AbstractBlock neighbor = iter.next();
      block.addBottomNeighbor(neighbor);
      if (!neighbor.getTopNeighbors().contains(block)) {
        neighbor.addTopNeighbor(block);
      }
    }
  }

  public void remove(AbstractBlock block) {
    collisionController.remove(BLOCK_OBJECT_TYPE, block);
    removeFromNeighbors(block);
    renderQueue.remove(block);
  }

  private void removeFromNeighbors(AbstractBlock<?> block) {
    for (AbstractBlock<?> neighbor : block.getLeftNeighbors()) {
      neighbor.removeRightNeighbor(block);
    }
    for (AbstractBlock<?> neighbor : block.getTopNeighbors()) {
      neighbor.removeBottomNeighbor(block);
    }
    for (AbstractBlock<?> neighbor : block.getRightNeighbors()) {
      neighbor.removeLeftNeighbor(block);
    }
    for (AbstractBlock<?> neighbor : block.getBottomNeighbors()) {
      neighbor.removeTopNeighbor(block);
    }
  }

  public Iterator<AbstractBlock> iterateAll() {
    return collisionController.<AbstractBlock>getAll(BLOCK_OBJECT_TYPE).iterator();
  }
}
