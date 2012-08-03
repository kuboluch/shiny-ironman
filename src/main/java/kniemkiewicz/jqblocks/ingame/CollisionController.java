package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.object.DebugRenderableShape;
import kniemkiewicz.jqblocks.ingame.object.block.AbstractBlock;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.util.IterableIterator;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.crypto.NodeSetData;
import java.util.List;

/**
 * User: knie
 * Date: 8/2/12
 */
@Component
public class CollisionController implements InputListener{

  @Autowired
  RenderQueue renderQueue;

  boolean debugModeTriggered = false;

  QuadTree<QuadTree.HasShape> quadTree = new QuadTree<QuadTree.HasShape>();

  boolean add(QuadTree.HasShape object) {
    return quadTree.add(object);
  }

  <T extends QuadTree.HasShape> List<T> fullSearch(Shape shape) {
    return (List<T>) quadTree.fullSearch(shape);
  }

  <T extends QuadTree.HasShape> IterableIterator<T> intersectsUnique(Shape shape) {
    return (IterableIterator<T>) quadTree.intersectsUnique(shape);
  }

  @Override
  public void listen(Input input, int delta) {
    if (KeyboardUtils.isDebugDisplayKeyPressed(input) && !debugModeTriggered) {
      debugModeTriggered = true;
      for (Rectangle rect : quadTree.getRects()) {
        renderQueue.add(new DebugRenderableShape(rect, Color.red));
      }
    }
  }

  public void remove(QuadTree.HasShape object) {
    quadTree.remove(object);
  }
}
