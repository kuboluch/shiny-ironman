package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.Enemy;
import kniemkiewicz.jqblocks.ingame.content.creature.SimpleBody;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 8/27/12
 */
public class Zombie implements UpdateQueue.ToBeUpdated<Zombie>, Enemy<Zombie>,
    AnimationRenderer.AnimationCompatible<SimpleBody> {

  private static final int MAX_HP = 50;
  public static final float HEIGHT = Sizes.BLOCK * 3.5f;
  static final float WIDTH = Sizes.BLOCK * 2.5f;
  static final float SPEED = Player.MAX_X_SPEED * 1 / 3;
  public static final float DEFAULT_X_DECELERATION = SPEED / 4f;

  static XYMovementDefinition ZOMBIE_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(SPEED).setDefaultDeceleration(DEFAULT_X_DECELERATION).setAutoDirection(false),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  long age = 0;

  final HealthPoints healthPoints;
  final Rectangle shape;
  final XYMovement movement;

  public Zombie(float x, float y) {
    healthPoints = new HealthPoints(MAX_HP, this);
    shape = new Rectangle(x, y, WIDTH, HEIGHT);
    movement = ZOMBIE_MOVEMENT.getMovement(x, y);
  }

  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this, true)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  private static BeanName<ZombieController> CONTROLLER = new BeanName<ZombieController>(ZombieController.class);

  @Override
  public BeanName<? extends HealthController<Zombie>> getHealthController() {
    return CONTROLLER;
  }

  private static final BeanName<AnimationRenderer> RENDERER = new BeanName<AnimationRenderer>(AnimationRenderer.class, "zombieRenderer");

  @Override
  public BeanName<AnimationRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Shape getShape() {
    return shape;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<Zombie>> getUpdateController() {
    return ZombieController.class;
  }

  @Override
  public XYMovement getXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    shape.setX(movement.getX());
    shape.setY(movement.getY());
  }

  @Override
  public long getAge() {
    return age;
  }

  public void setAge(long age) {
    this.age = age;
  }
}
