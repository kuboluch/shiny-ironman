package kniemkiewicz.jqblocks.ingame.content.player;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.renderer.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.production.CanProduce;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public class Player implements PhysicalObject, HasHealthPoints<Player>, TwoFacedImageRenderer.Renderable,
    HasFullXYMovement, CanProduce {

  private static final long serialVersionUID = 1;

  private static int INITIAL_HP = 1000;
  // Used in Spring.
  public static int IMAGE_WIDTH = Sizes.BLOCK * 3;

  XYMovement xyMovement;
  private Rectangle shape;
  private HealthPoints healthPoints;

  public static int HEIGHT = Sizes.BLOCK * 4 - 3;
  public static int WIDTH = 2 * Sizes.BLOCK - 5;
  public static final float MAX_X_SPEED = Sizes.BLOCK * 4;
  public static final float DEFAULT_X_DECELERATION = MAX_X_SPEED / 2.5f;
  public static final float JUMP_SPEED = Sizes.MAX_FALL_SPEED / 3;
  public static final float MAX_LADDER_SPEED = Sizes.MAX_FALL_SPEED / 9;

  static XYMovementDefinition PLAYER_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(MAX_X_SPEED).setDefaultDeceleration(DEFAULT_X_DECELERATION).setAutoDirection(false).setMaxSpeedBackward(MAX_X_SPEED / 2),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );


  public Player() {
    xyMovement = PLAYER_MOVEMENT.getMovement(0, 0);
    shape = new Rectangle(0, 0, WIDTH, HEIGHT);
    healthPoints = new HealthPoints(INITIAL_HP, this);
  }

  void addTo(RenderQueue rq, MovingObjects movingObjects) {
    rq.add(this);
    movingObjects.add(this, true);
  }

  private static final BeanName<TwoFacedImageRenderer> RENDERER = new BeanName<TwoFacedImageRenderer>(TwoFacedImageRenderer.class, "playerRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
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
    return !xyMovement.getXMovement().getDirection();
  }

  @Override
  public XYMovement getXYMovement() {
    return xyMovement;
  }
}
