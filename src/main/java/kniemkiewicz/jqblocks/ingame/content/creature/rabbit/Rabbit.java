package kniemkiewicz.jqblocks.ingame.content.creature.rabbit;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import static kniemkiewicz.jqblocks.ingame.content.creature.rabbit.RabbitDefinition.*;

/**
 * User: qba
 * Date: 23.09.12
 */
public class Rabbit implements RenderableObject<Rabbit>,UpdateQueue.ToBeUpdated<Rabbit>, KillablePhysicalObject<Rabbit>,
    HasFullXYMovement {

  Rectangle rectangle;
  XYMovement movement;

  HealthPoints healthPoints = new HealthPoints(MAX_HP, this);

  enum State {
    STILL,
    MOVING,
    RUNNING,
    RUNNING_AWAY
  }

  State state = State.MOVING;

  public Rabbit(float x, float y) {
    this.movement = RABBIT_MOVEMENT.getMovement(x, y);
    rectangle = new Rectangle(x, y, WIDTH, HEIGHT);
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
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

  @Override
  public HealthPoints getHp() {
    return healthPoints;
  }

  @Override
  public BeanName<? extends HealthController<Rabbit>> getHealthController() {
    return CONTROLLER;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Rectangle getShape() {
    return rectangle;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<Rabbit>> getUpdateController() {
    return RabbitController.class;
  }

  // Do not add panelItems manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this, false)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }
}
