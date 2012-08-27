package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ZombieRenderer implements ObjectRenderer<Zombie> {
  @Override
  public void render(Zombie object, Graphics g, PointOfView pov) {
    g.setColor(Color.black);
    g.fill(object.shape);
  }
}
