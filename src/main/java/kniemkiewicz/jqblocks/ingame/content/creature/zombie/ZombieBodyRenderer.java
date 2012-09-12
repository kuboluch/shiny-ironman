package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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

  static final float WIDTH = 3.5f * Sizes.BLOCK;
  static final float WIDTH_DIFF = (WIDTH - Zombie.WIDTH) / 2;

  @Override
  public void render(ZombieBody object, Graphics g, PointOfView pov) {
    int spriteId = Math.min(object.getAge() / 125, 6);
    Rectangle r = object.getShape();
    Image sprite = zombieDeathSheet.getSprite(spriteId, 0);
    if (object.getXYMovement().getXMovement().getDirection()) {
      sprite = sprite.getFlippedCopy(true, false);
    }
    sprite.draw(r.getX() - WIDTH_DIFF, r.getY() - 0.6f * Sizes.BLOCK, WIDTH, r.getHeight() + Sizes.BLOCK);
  }
}
