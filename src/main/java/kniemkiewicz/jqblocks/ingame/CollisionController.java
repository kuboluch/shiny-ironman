package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.object.DebugRenderableShape;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

  void add(QuadTree.HasShape object) {
    quadTree.add(object);
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
}
