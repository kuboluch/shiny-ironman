package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: krzysiek
 * Date: 17.09.12
 */
@Component
public class FireballRenderer implements ObjectRenderer<Fireball> {
  //TODO: if we're going to use particleSystem in any other class, we should make one common object which does updates
  // and scaling.
  @Autowired
  FireballEquippedItemRenderer fireballEquippedItemRenderer;

  class FireballData {
    boolean wasRendered = false;
    ParticleSystem system;
  }

  Map<Fireball, FireballData> data = new HashMap<Fireball, FireballData>();

  public void update(int delta) {
    Iterator<Map.Entry<Fireball, FireballData>> it = data.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Fireball, FireballData> entry = it.next();
      if (entry.getValue().wasRendered) {
        entry.getValue().system.update(delta);
      } else {
        it.remove();
      }
    }
  }

  @Override
  public void render(Fireball object, Graphics g, PointOfView pov) {
    FireballData d = null;
    if (!data.containsKey(object)) {
       d = new FireballData();
      d.system = fireballEquippedItemRenderer.newFireSystem();
      data.put(object, d);
    }
    if (d == null) {
      d = data.get(object);
    }
    d.wasRendered = true;
    renderSystem(g, object, d.system, pov);
  }

  private void renderSystem(Graphics g, Fireball object, ParticleSystem system, PointOfView pov) {
    float sx = 0.2f;
    float sy = 0.3f;
    Vector2f speed = object.getMovement().getSpeed();
    g.pushTransform();
    g.translate(object.getShape().getCenterX(), object.getShape().getCenterY());
    g.rotate(0,0, (float)speed.getTheta() - 90);
    g.scale(sx, sy);
    system.render(-object.getShape().getWidth() / 2, -object.getShape().getHeight() / 2);
    g.popTransform();
  }
}
