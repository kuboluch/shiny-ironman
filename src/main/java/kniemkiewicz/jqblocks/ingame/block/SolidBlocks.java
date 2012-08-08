package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.block.EndOfTheWorldWall;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import kniemkiewicz.jqblocks.util.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class SolidBlocks{

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  CollisionController collisionController;

  @Autowired
  SpringBeanProvider springBeanProvider;

  RawEnumTable<WallBlockType> blocks;

  @Autowired
  TimingInfo timingInfo;

  public SolidBlocks() {
    int width = (Sizes.MAX_X - Sizes.MIN_X) / Sizes.BLOCK;
    int length = (Sizes.MAX_Y - Sizes.MIN_Y) / Sizes.BLOCK;
    blocks = new RawEnumTable<WallBlockType>(WallBlockType.EMPTY, width, length);
  }

  static final EnumSet<CollisionController.ObjectType> BLOCK_OBJECT_TYPE = EnumSet.of(CollisionController.ObjectType.WALL);

  @PostConstruct
  void init() {
    List<AbstractBlock> blocks = new ArrayList<AbstractBlock>();
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MAX_Y, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    blocks.add(new EndOfTheWorldWall(Sizes.MAX_X, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    for (AbstractBlock b : blocks) {
      Assert.executeAndAssert(collisionController.add(BLOCK_OBJECT_TYPE, b, true));
    }
    this.blocks.fillRendererCache(springBeanProvider);
    renderQueue.add(this.blocks);
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
    Assert.executeAndAssert(collisionController.add(BLOCK_OBJECT_TYPE, block, true));
    blocks.setRectUnscaled(block.getShape(), WallBlockType.DIRT);
    return true;
  }

  public void remove(AbstractBlock block) {
    collisionController.remove(BLOCK_OBJECT_TYPE, block);
  }

  public void serializeData(ObjectOutputStream stream) throws IOException {
    stream.writeObject(blocks);
  }

  public void deserializeData(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    blocks = (RawEnumTable<WallBlockType>) stream.readObject();
    blocks.fillRendererCache(springBeanProvider);
  }

  public RawEnumTable<WallBlockType> getBlocks() {
    return blocks;
  }
}
