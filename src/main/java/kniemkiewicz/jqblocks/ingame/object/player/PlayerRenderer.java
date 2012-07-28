package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 7/28/12
 */
@Component
public class PlayerRenderer implements ObjectRenderer<Player> {

  @Resource(name = "playerLeftImage")
  private Image leftImage;
  @Resource(name = "playerRightImage")
  private Image rightImage;

  public static int IMAGE_WIDTH = Sizes.BLOCK * 3;
  @Override
  public void render(Player p, Graphics g, PointOfView pov) {
    g.setColor(Color.white);
    // g.draw(shape);
    if (p.leftFaced) {
      leftImage.draw((int)p.xMovement.getPos(), (int)p.yMovement.getPos(), IMAGE_WIDTH, Player.HEIGHT);
    } else {
      rightImage.draw((int)p.xMovement.getPos() - (IMAGE_WIDTH - Player.WIDTH), (int)p.yMovement.getPos(), IMAGE_WIDTH, Player.HEIGHT);
    }
  }
}
