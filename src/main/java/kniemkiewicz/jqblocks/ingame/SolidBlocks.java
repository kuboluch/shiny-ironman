package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.object.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.object.EndOfTheWorldWall;
import kniemkiewicz.jqblocks.ingame.util.LinearIntersectionIterator;
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

  Set<AbstractBlock> blocks = new HashSet<AbstractBlock>();

  @PostConstruct
  void init() {
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MAX_Y, Sizes.MAX_X - Sizes.MIN_X + 2000, 1000));
    blocks.add(new EndOfTheWorldWall(Sizes.MIN_X - 1000, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    blocks.add(new EndOfTheWorldWall(Sizes.MAX_X, Sizes.MIN_Y - 1000, 1000, Sizes.MAX_Y - Sizes.MIN_Y + 2000));
    for (AbstractBlock b : blocks) {
      renderQueue.add(b);
    }
    /*int x = (Sizes.MIN_X + Sizes.MAX_X) / 2;
    int y = Sizes.MAX_Y / 6 + 150;
    add(new DirtBlock(x, y, 2 * Sizes.BLOCK, 2 * Sizes.BLOCK));*/
  }
  
  public Set<AbstractBlock> getBlocks() {
    return Collections.unmodifiableSet(blocks);
  }

  public Iterator<AbstractBlock> intersects(Rectangle rect) {
    return new LinearIntersectionIterator(blocks.iterator(), rect);
  }

  public boolean add(AbstractBlock block) {
    if (intersects(block.getShape()).hasNext()) return false;
    if (movingObjects.intersects(block.getShape()).hasNext()) return false;
    blocks.add(block);
    renderQueue.add(block);
    return true;
  }

  public void remove(AbstractBlock block) {
    blocks.remove(block);
    renderQueue.remove(block);
  }
}
