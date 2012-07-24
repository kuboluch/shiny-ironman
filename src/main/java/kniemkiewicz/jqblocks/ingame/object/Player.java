package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.util.LimitedSpeed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
@Component
public class Player implements RenderableObject,PhysicalObject {

  public static Log logger = LogFactory.getLog(Player.class);

  @Resource(name = "playerLeftImage")
  private Image leftImage;
  @Resource(name = "playerRightImage")
  private Image rightImage;
  boolean leftFaced;
  private LimitedSpeed xMovement;
  private LimitedSpeed yMovement;
  private Rectangle shape;

  public static int HEIGHT = Sizes.BLOCK * 4 - 3;
  public static int WIDTH = 2 * Sizes.BLOCK - 5;
  public static int IMAGE_WIDTH = Sizes.BLOCK * 3;
  public static final float MAX_X_SPEED = Sizes.BLOCK / Sizes.TIME_UNIT * 4;
  public static final float X_ACCELERATION = MAX_X_SPEED / Sizes.TIME_UNIT / 3.75f;
  public static final float DEFAULT_X_DECELERATION = MAX_X_SPEED / Sizes.TIME_UNIT / 2.5f;
  public static final float JUMP_SPEED = Sizes.MAX_FALL_SPEED / 3;

  public Player() {
    xMovement = new LimitedSpeed(MAX_X_SPEED, 0, 0, DEFAULT_X_DECELERATION);
    yMovement = new LimitedSpeed(Sizes.MAX_FALL_SPEED, 0, 0, 0);
    shape = new Rectangle(xMovement.getPos(), yMovement.getPos(), WIDTH - 1, HEIGHT - 1);
  }

  public void renderObject(Graphics g, PointOfView pov) {
    //logger.debug("player [x="+xMovement.getPos()+", y="+yMovement.getPos()+"]");
    g.setColor(Color.white);
   // g.draw(shape);
    if (leftFaced) {
      leftImage.draw((int)xMovement.getPos(), (int)yMovement.getPos(), IMAGE_WIDTH, HEIGHT);
    } else {
      rightImage.draw((int)xMovement.getPos() - (IMAGE_WIDTH - WIDTH), (int)yMovement.getPos(), IMAGE_WIDTH, HEIGHT);
    }
  }
  
  public LimitedSpeed getXMovement() {
    return xMovement;
  }

  public LimitedSpeed getYMovement() {
    return yMovement;
  }

  public void update(int delta) {
    xMovement.update(delta);
    yMovement.update(delta);
    if (xMovement.getSpeed() != 0) {
      leftFaced = xMovement.getSpeed() < 0;
    }
    updateShape();
  }

  public void updateShape() {
    shape.setX(xMovement.getPos());
    shape.setY(yMovement.getPos());
  }

  public Rectangle getShape() {
    return shape;
  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }


  @Override
  public String toString() {
    return "Player{" +
        "leftFaced=" + leftFaced +
        ", xMovement=" + xMovement +
        ", yMovement=" + yMovement +
        '}';
  }
}
