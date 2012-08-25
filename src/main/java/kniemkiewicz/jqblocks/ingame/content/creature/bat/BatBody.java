package kniemkiewicz.jqblocks.ingame.content.creature.bat;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.content.player.Player;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.util.FullXYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: knie
 * Date: 7/24/12
 */
public class BatBody implements RenderableObject<BatBody>, HasFullXYMovement {

  FullXYMovement movement;
  Rectangle rectangle;

  public static final float X_SPEED = Bat.X_SPEED;
  public static final float Y_INITIAL_SPEED = Player.JUMP_SPEED / 4;

  public BatBody(float x, float y, int direction) {
    movement = new FullXYMovement(x, y, direction * X_SPEED, Sizes.MAX_FALL_SPEED);
    movement.getXMovement().setSpeed(direction * X_SPEED);
    movement.getYMovement().setSpeed(- Y_INITIAL_SPEED);
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
  public FullXYMovement getFullXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    rectangle.setX(movement.getX());
    rectangle.setY(movement.getY());
  }
}
