package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.serialization.SerializableRef;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: knie
 * Date: 7/24/12
 */
public class Bat implements RenderableObject<Bat>, UpdateQueue.ToBeUpdated<Bat>, KillablePhysicalObject<Bat>, HasFullXYMovement {

  Rectangle rectangle;
  XYMovement movement;
  public static final float SPEED = Player.MAX_X_SPEED / 3;
  public static final float ACCELERATION = SPEED;
  public static final int SIZE = 2 * Sizes.BLOCK;
  private static int BAT_BP = 5;
  HealthPoints healthPoints = new HealthPoints(BAT_BP, this);
  SerializableRef<PhysicalObject> target = new SerializableRef<PhysicalObject>();
  // Used only when bat is chasing target.
  Vector2f accelerationVector = null;

  static XYMovementDefinition BAT_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(SPEED),
      new MovementDefinition().setMaxSpeed(SPEED)
  );


  public Bat(float x, float y) {
    this.movement = BAT_MOVEMENT.getMovement(x, y).setXSpeed(SPEED);
    rectangle = new Rectangle(x, y, SIZE, SIZE);
  }

  // Do not add panelItems manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this, false)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRendererImpl.class, "batRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Rectangle getShape() {
    return rectangle;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<Bat>> getUpdateController() {
    return BatController.class;
  }

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  private static final BeanName<BatController> CONTROLLER = new BeanName<BatController>(BatController.class);

  @Override
  public BeanName<? extends HealthController<Bat>> getHealthController() {
    return CONTROLLER;
  }

  public PhysicalObject getTarget() {
    return target.get();
  }

  public void setTarget(PhysicalObject target) {
    this.target.set(target);
  }

  public Vector2f getAccelerationVector() {
    return accelerationVector;
  }

  public void setAccelerationVector(Vector2f accelerationVector) {
    this.accelerationVector = accelerationVector;
  }

  @Override
  public XYMovement getXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    rectangle.setX(movement.getX());
    rectangle.setY(movement.getY());
  }
}
