package kniemkiewicz.jqblocks.ingame.content.creature.peon;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.Path;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.PermPath;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.renderer.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public class Peon implements PhysicalObject, KillablePhysicalObject<Peon>, TwoFacedImageRenderer.Renderable, UpdateQueue.ToBeUpdated<Peon>, HasFullXYMovement{

  private static final int PEON_HP = 100;
  static final float MAX_PEON_SPEED = Player.MAX_X_SPEED / 600;

  public static final float WIDTH = Sizes.BLOCK * 2 + 5;
  public static final float HEIGHT = Sizes.BLOCK * 3 + 5;

  transient PermPath currentPath = null;
  HealthPoints healthPoints = new HealthPoints(PEON_HP, this);
  final Rectangle shape;

  XYMovement movement;

  static XYMovementDefinition PEON_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(MAX_PEON_SPEED).setAutoDirection(false),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  public static Peon createAndRegister(int x, int y, PeonController controller) {
    Peon peon = new Peon(x, y);
    if (!controller.register(peon)) return null;
    return peon;
  }

  private Peon(int x, int y) {
    movement = PEON_MOVEMENT.getMovement(x, y);
    shape = new Rectangle(x  - WIDTH / 2, y, WIDTH, HEIGHT);
  }

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  private static final BeanName<PeonController> CONTROLLER = new BeanName<PeonController>(PeonController.class);

  @Override
  public BeanName<? extends HealthController<Peon>> getHealthController() {
    return CONTROLLER;
  }

  @Override
  public boolean isLeftFaced() {
    return !movement.getXMovement().getDirection();
  }

  private static final BeanName<TwoFacedImageRenderer> RENDERER = new BeanName<TwoFacedImageRenderer>(TwoFacedImageRenderer.class, "peonRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Rectangle getShape() {
    return shape;
  }

  PermPath getCurrentPath() {
    return currentPath;
  }

  public void setCurrentPath(PermPath currentPath) {
    this.currentPath = currentPath;
  }

  void update() {
    if (currentPath != null) {
      float x = movement.getX();
      Vector2f newPos = currentPath.getStartPoint();
      movement.getXMovement().setPos(newPos.getX());
      movement.getYMovement().setPos(newPos.getY() - HEIGHT);
      if (x < newPos.getX()) movement.getXMovement().setDirection(true);
      if (x > newPos.getX()) movement.getXMovement().setDirection(false);
      updateShape();
    }
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<? super Peon>> getUpdateController() {
    return PeonController.class;
  }

  @Override
  public XYMovement getXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    shape.setX(movement.getX() - WIDTH / 2);
    shape.setY(movement.getY());
  }
}
