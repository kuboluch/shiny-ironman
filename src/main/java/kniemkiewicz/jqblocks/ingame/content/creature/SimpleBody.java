package kniemkiewicz.jqblocks.ingame.content.creature;

import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 10.09.12
 */
public abstract class SimpleBody implements RenderableObject<SimpleBody>,HasFullXYMovement {
  protected final XYMovement movement;
  final Rectangle rectangle;

  public SimpleBody(XYMovement movement, float size) {
    rectangle = new Rectangle(movement.getX(), movement.getY(), size, size);
    this.movement = movement;
  }

  // This method is protected so that any subclasses can hide it and add one with more options if needed
  protected boolean addTo(RenderQueue renderQueue, FreeFallController freeFallController) {
    renderQueue.add(this);
    freeFallController.addComplex(this);
    return true;
  }

  @Override
  public abstract BeanName<? extends ObjectRenderer> getRenderer();

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
  public XYMovement getXYMovement() {
    return movement;
  }

  @Override
  public void updateShape() {
    rectangle.setX(movement.getX());
    rectangle.setY(movement.getY());
  }
}
