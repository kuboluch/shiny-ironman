package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 11.09.12
 */
@Component
public class ZombieBodyRenderer implements ObjectRenderer<ZombieBody> {

  @Resource
  SpriteSheet zombieDeathSheet;

  @Override
  public void render(ZombieBody object, Graphics g, PointOfView pov) {
    int spriteId = Math.min(object.getAge() / 100, 6);
    Rectangle r = object.getShape();
    zombieDeathSheet.getSprite(spriteId, 0).draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
  }
}
