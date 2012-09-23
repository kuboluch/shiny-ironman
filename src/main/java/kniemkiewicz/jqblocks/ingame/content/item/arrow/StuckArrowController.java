package kniemkiewicz.jqblocks.ingame.content.item.arrow;

import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 12.09.12
 */
@Component
public class StuckArrowController implements UpdateQueue.UpdateController<StuckArrow> {

  @Autowired
  RenderQueue renderQueue;

  @Autowired
  UpdateQueue updateQueue;

  @Override
  public void update(StuckArrow object, int delta) {
    if (object.getTarget().getHp().isDead()) {
      renderQueue.remove(object);
      updateQueue.remove(object);
    } else {
      object.update();
    }
  }
}
