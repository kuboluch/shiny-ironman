package kniemkiewicz.jqblocks.ingame.content.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.util.FullXYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Player implements PhysicalObject,HasHealthPoints<Player>, TwoFacedImageRenderer.Renderable, HasFullXYMovement {

  private static final long serialVersionUID = 1;

  private static int INITIAL_HP = 1000;
  // Used in Spring.
  public static int IMAGE_WIDTH = Sizes.BLOCK * 3;

  FullXYMovement xyMovement;
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
    xyMovement = new FullXYMovement(0, 0, MAX_X_SPEED, Sizes.MAX_FALL_SPEED);
    xyMovement.getXMovement().setDefaultDeceleration(DEFAULT_X_DECELERATION);
    shape = new Rectangle(0, 0, WIDTH, HEIGHT);
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

  public void update(int delta) {
    xyMovement.update(delta);
    updateShape();
  }

  public void updateShape() {
    shape.setX(xyMovement.getX());
    shape.setY(xyMovement.getY());
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
        "leftFaced=" + isLeftFaced() +
        ", x=" + xyMovement.getX() +
        ", y=" + xyMovement.getY() +
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
    return !xyMovement.getXMovement().getLastDirection();
  }

  @Override
  public FullXYMovement getFullXYMovement() {
    return xyMovement;
  }
}
