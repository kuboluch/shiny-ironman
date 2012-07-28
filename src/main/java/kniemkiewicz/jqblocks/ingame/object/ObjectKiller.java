package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 7/25/12
 */
@Component
public class ObjectKiller {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  MovingObjects movingObjects;

  @Autowired
  UpdateQueue updateQueue;

  public void killMovingObject(Object object) {
    if (object instanceof RenderableObject) {
      renderQueue.remove((RenderableObject) object);
    }
    if (object instanceof PhysicalObject) {
      movingObjects.remove((PhysicalObject) object);
    }
    if (object instanceof UpdateQueue.ToBeUpdated) {
      updateQueue.remove((UpdateQueue.ToBeUpdated)object);
    }
  }
}
