package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
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
    blocks = new RawEnumTable<WallBlockType>(WallBlockType.EMPTY, WallBlockType.SPACE, width, length);
  }

  @PostConstruct
  void init() {
    this.blocks.fillRendererCache(springBeanProvider);
    renderQueue.add(this.blocks);
  }

  public boolean add(Rectangle block, WallBlockType type) {
    if (blocks.collidesWithNonEmpty(block)) return false;
    if (collisionController.intersects(MovingObjects.OBJECT_TYPES, block)) return false;
    blocks.setRectUnscaled(block, type);
    return true;
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

  public boolean isOnSolidGround(Shape shape) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    RawEnumTable<WallBlockType> table = blocks;
    int y = table.toYIndex((int) rect.getMaxY() + 1);
    int x1 = table.toXIndex((int) rect.getX());
    int x2 = table.toXIndex((int) rect.getMaxX() - 1);
    for (int x = x1; x < x2; x++) {
      if (table.getValueForUnscaledPoint(x, y) == WallBlockType.EMPTY) {
        return false;
      }
    }
    return true;
  }
}
