package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: knie
 * Date: 7/24/12
 */
public class BatBody implements RenderableObject<BatBody>, HasFullXYMovement {

  XYMovement movement;
  Rectangle rectangle;

  public static final float X_SPEED = Bat.X_SPEED;
  public static final float Y_INITIAL_SPEED = Player.JUMP_SPEED / 4;

  static XYMovementDefinition BAT_BODY_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition().setMaxSpeed(X_SPEED),
      new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  public BatBody(float x, float y, int direction) {
    movement = BAT_BODY_MOVEMENT.getMovement(x, y).setXSpeed(direction * X_SPEED).setYSpeed(- Y_INITIAL_SPEED);
    rectangle = new Rectangle(x, y, Bat.SIZE, Bat.SIZE);
  }

  // Do not add objects manually. Using this method makes sure you won't forget any part.
  public boolean addTo(RenderQueue renderQueue, FreeFallController freeFallController) {
    renderQueue.add(this);
    freeFallController.addComplex(this);
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "batBodyRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super BatBody>> getRenderer() {
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
  public XYMovement getFullXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    rectangle.setX(movement.getX());
    rectangle.setY(movement.getY());
  }
}
