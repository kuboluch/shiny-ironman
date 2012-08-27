package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 8/27/12
 */
@Component
public class ZombieRenderer implements ObjectRenderer<Zombie> {

  @Resource
  Image zombieImage;

  @Override
  public void render(Zombie object, Graphics g, PointOfView pov) {
    zombieImage.draw(object.getFullXYMovement().getX(), object.getFullXYMovement().getY(),
        Zombie.WIDTH, Zombie.HEIGHT);
  }
}
