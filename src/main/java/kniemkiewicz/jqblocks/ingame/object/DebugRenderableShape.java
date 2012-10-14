package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.controller.ai.paths.Joint;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * User: krzysiek
 * Date: 17.07.12
 */
public class DebugRenderableShape implements RenderableObject<DebugRenderableShape>{

  final Shape shape;
  final Color color;

  public DebugRenderableShape(Shape shape, Color color) {
    this.shape = shape;
    this.color = color;
  }

  public DebugRenderableShape(Vector2f point, Color color) {
    Rectangle r = new Rectangle(0,0,6,6);
    r.setCenterX(point.getX());
    r.setCenterY(point.getY());
    this.shape = r;
    this.color = color;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(color);
    g.draw(shape);
  }

  public Shape getShape() {
    return shape;
  }

  @Override
  public Layer getLayer() {
    return Layer.PLUS_INF;
  }
}
