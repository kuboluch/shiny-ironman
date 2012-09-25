package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 25.09.12
 */
@Component
public class FireballExplosionController implements UpdateQueue.UpdateController<FireballExplosion> {

  @Autowired
  RenderQueue renderQueue;

  @Override
  public void update(FireballExplosion object, int delta) {
    object.age += delta;
    if (object.age > FireballExplosion.MAX_AGE) {
      renderQueue.remove(object);
    }
  }
}
