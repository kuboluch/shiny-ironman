package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectKiller;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
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
public class Player implements RenderableObject<Player>,PhysicalObject,HasHealthPoints {

  private static int INITIAL_HP = 100;

  boolean leftFaced;
  LimitedSpeed xMovement;
  LimitedSpeed yMovement;
  private Rectangle shape;
  private HealthPoints healthPoints;

  public static int HEIGHT = Sizes.BLOCK * 4 - 3;
  public static int WIDTH = 2 * Sizes.BLOCK - 5;
  public static final float MAX_X_SPEED = Sizes.BLOCK / Sizes.TIME_UNIT * 4;
  public static final float X_ACCELERATION = MAX_X_SPEED / Sizes.TIME_UNIT / 3.75f;
  public static final float DEFAULT_X_DECELERATION = MAX_X_SPEED / Sizes.TIME_UNIT / 2.5f;
  public static final float JUMP_SPEED = Sizes.MAX_FALL_SPEED / 3;

  public Player() {
    xMovement = new LimitedSpeed(MAX_X_SPEED, 0, 0, DEFAULT_X_DECELERATION);
    yMovement = new LimitedSpeed(Sizes.MAX_FALL_SPEED, 0, 0, 0);
    shape = new Rectangle(xMovement.getPos(), yMovement.getPos(), WIDTH - 1, HEIGHT - 1);
    healthPoints = new HealthPoints(INITIAL_HP, this);
  }

  void addTo(RenderQueue rq, MovingObjects movingObjects) {
    rq.add(this);
    movingObjects.add(this);
  }

  @Override
  public Class<? extends ObjectRenderer<Player>> getRenderer() {
    return PlayerRenderer.class;
  }

  public void renderObject(Graphics g, PointOfView pov) { }
  
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

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  @Override
  public void killed(ObjectKiller killer) {
    // for now player cannot be killed.
  }
}
