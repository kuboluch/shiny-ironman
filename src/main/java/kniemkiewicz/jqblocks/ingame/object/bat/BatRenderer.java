package kniemkiewicz.jqblocks.ingame.object.bat;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/24/12
 */
@Component
public class BatRenderer implements ObjectRenderer<Bat> {

  @Resource(name = "batImage")
  Image image;

  @Override
  public void render(Bat object, Graphics g, PointOfView pov) {
    image.draw(object.xMovement.getPos(), object.y, Bat.SIZE, Bat.SIZE);
  }
}
