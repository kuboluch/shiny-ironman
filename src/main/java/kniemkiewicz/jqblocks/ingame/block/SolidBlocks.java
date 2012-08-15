package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.player.PlayerController;
import kniemkiewicz.jqblocks.ingame.ui.info.TimingInfo;
import kniemkiewicz.jqblocks.util.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.XMLPackedSheet;
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
    blocks = new RawEnumTable<WallBlockType>(WallBlockType.EMPTY, WallBlockType.SPACE);
  }

  protected SolidBlocks(RawEnumTable<WallBlockType> blocks) {
    this.blocks = blocks;
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

  // TODO add some tests
  public boolean isOnSolidGround(Shape shape) {
    Rectangle rect = GeometryUtils.getNewBoundingRectangle(shape);
    if (blocks.collidesWithNonEmpty(rect)) {
      return false;
    }
    int y = (int) (rect.getY() + (int) rect.getHeight() + 1);
    int x1 = (int) rect.getX();
    int x2 = (int) rect.getX() + (int) rect.getWidth();
    for (int x = x1; x < x2; x += Sizes.BLOCK) {
      if (blocks.getValueForUnscaledPoint(x, y) == WallBlockType.EMPTY) {
        return false;
      }
    }
    return true;
  }
}
