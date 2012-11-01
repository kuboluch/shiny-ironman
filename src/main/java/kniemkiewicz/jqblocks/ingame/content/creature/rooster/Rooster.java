package kniemkiewicz.jqblocks.ingame.content.creature.rooster;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.content.creature.Enemy;
import kniemkiewicz.jqblocks.ingame.content.creature.Neutral;
import kniemkiewicz.jqblocks.ingame.controller.MovingObjects;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.renderer.creature.RunningCreatureRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import static kniemkiewicz.jqblocks.ingame.content.creature.rooster.RoosterDefinition.*;

/**
 * User: qba
 * Date: 08.10.12
 */
public class Rooster implements RenderableObject<Rooster>, UpdateQueue.ToBeUpdated<Rooster>, Neutral<Rooster>,
    HasFullXYMovement, RunningCreatureRenderer.RunningCreature<Rooster> {

  Rectangle rectangle;
  XYMovement movement;
  Enemy enemySpotted;

  HealthPoints healthPoints = new HealthPoints(MAX_HP, this);

  enum State {
    STILL,
    MOVING,
    RUNNING_AWAY
  }

  State state = State.MOVING;

  public Rooster(float x, float y) {
    this.movement = MOVEMENT.getMovement(x, y);
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
  public BeanName<? extends HealthController<Rooster>> getHealthController() {
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
  public Class<? extends UpdateQueue.UpdateController<Rooster>> getUpdateController() {
    return RoosterController.class;
  }

  // Do not add panelItems manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this, false)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }

  public Enemy getEnemySpotted() {
    return enemySpotted;
  }

  public void setEnemySpotted(Enemy enemySpotted) {
    this.enemySpotted = enemySpotted;
  }

  long age = 0;

  @Override
  public long getAge() {
    return age;
  }

  public void setAge(long age) {
    this.age = age;
  }

  @Override
  public boolean isRunning() {
    return state == State.RUNNING_AWAY;
  }
}