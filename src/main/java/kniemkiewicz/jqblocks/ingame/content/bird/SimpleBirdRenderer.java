package kniemkiewicz.jqblocks.ingame.content.bird;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.util.Direction;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 23.09.12
 */
@Component
public class SimpleBirdRenderer implements ObjectRenderer<Bird> {

  @Resource
  SpriteSheet birdsSpriteSheet;

  @Override
  public void render(Bird object, Graphics g, PointOfView pov) {
    Bird.BirdColor color = object.getBirdColor();
    Rectangle r = object.getShape();
    int frameId = (int) ((object.getAge() / 300) % 3);
    birdsSpriteSheet.getSprite(3 * color.getX() + frameId, color.getY() * 2 + (object.getDirection() == Direction.RIGHT ? 1 : 0)).draw(r.getX(), r.getY(), r.getWidth(), r.getHeight());
  }
}
