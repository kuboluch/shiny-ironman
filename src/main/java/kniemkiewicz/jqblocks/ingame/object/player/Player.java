package kniemkiewicz.jqblocks.ingame.object.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.util.LimitedSpeed;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Player implements PhysicalObject,HasHealthPoints<Player>, TwoFacedImageRenderer.Renderable {

  private static final long serialVersionUID = 1;

  private static int INITIAL_HP = 1000;
  // Used in Spring.
  public static int IMAGE_WIDTH = Sizes.BLOCK * 3;

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
  public static final float MAX_LADDER_SPEED = Sizes.MAX_FALL_SPEED / 9;

  public Player() {
    xMovement = new LimitedSpeed(MAX_X_SPEED, 0, 0, DEFAULT_X_DECELERATION);
    yMovement = new LimitedSpeed(Sizes.MAX_FALL_SPEED, 0, 0, 0);
    shape = new Rectangle(xMovement.getPos(), yMovement.getPos(), WIDTH, HEIGHT);
    healthPoints = new HealthPoints(INITIAL_HP, this);
  }

  void addTo(RenderQueue rq, MovingObjects movingObjects) {
    rq.add(this);
    movingObjects.add(this);
  }

  private static final BeanName<TwoFacedImageRenderer> RENDERER = new BeanName<TwoFacedImageRenderer>(TwoFacedImageRenderer.class, "playerRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<TwoFacedImageRenderer.Renderable>> getRenderer() {
    return RENDERER;
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

  private static final BeanName<PlayerController> CONTROLLER = new BeanName<PlayerController>(PlayerController.class);

  @Override
  public BeanName<? extends HealthController<Player>> getHealthController() {
    return CONTROLLER;
  }

  @Override
  public boolean isLeftFaced() {
    return leftFaced;
  }
}
