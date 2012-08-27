package kniemkiewicz.jqblocks.ingame.content.creature.zombie;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.creature.bat.Bat;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.FullXYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 8/27/12
 */
public class Zombie implements RenderableObject<Zombie>,UpdateQueue.ToBeUpdated<Zombie>,HasHealthPoints<Zombie>, HasFullXYMovement {

  private static final int MAX_HP = 50;
  public static final float HEIGHT = Sizes.BLOCK * 3.5f;
  private static final float WIDTH = Sizes.BLOCK * 2.5f;
  private static final float SPEED = Player.MAX_X_SPEED * 2 / 3;

  final HealthPoints healthPoints;
  final Rectangle shape;
  final FullXYMovement movement;

  public Zombie(float x, float y) {
    healthPoints = new HealthPoints(MAX_HP, this);
    shape = new Rectangle(x, y, WIDTH, HEIGHT);
    movement = new FullXYMovement(x, y, SPEED, Sizes.MAX_FALL_SPEED);
  }

  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this)) return false;
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

  private static BeanName<ZombieRenderer> RENDERER = new BeanName<ZombieRenderer>(ZombieRenderer.class);

  @Override
  public BeanName<? extends ObjectRenderer<? super Zombie>> getRenderer() {
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
  public FullXYMovement getFullXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    shape.setX(movement.getX());
    shape.setY(movement.getY());
  }
}
