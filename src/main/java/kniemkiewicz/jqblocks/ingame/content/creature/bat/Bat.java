package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.content.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthController;
import kniemkiewicz.jqblocks.ingame.content.hp.HealthPoints;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.util.HorizontalMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: knie
 * Date: 7/24/12
 */
public class Bat implements RenderableObject<Bat>,UpdateQueue.ToBeUpdated<Bat>,HasHealthPoints<Bat> {

  Rectangle rectangle;
  HorizontalMovement movement;
  public static final float X_SPEED = Player.MAX_X_SPEED / 3;
  public static final int SIZE = 2 * Sizes.BLOCK;
  private static int BAT_BP = 5;
  HealthPoints healthPoints = new HealthPoints(BAT_BP, this);

  public Bat(int x, int y) {
    this.movement = new HorizontalMovement(x, y, X_SPEED);
    this.movement.getXMovement().setSpeed(X_SPEED);
    rectangle = new Rectangle(x, y, SIZE, SIZE);
  }

  // Do not add objects manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue, UpdateQueue updateQueue) {
    if (!movingObjects.add(this)) return false;
    renderQueue.add(this);
    updateQueue.add(this);
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "batRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super Bat>> getRenderer() {
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

  public HorizontalMovement getMovement() {
    return movement;
  }
}
