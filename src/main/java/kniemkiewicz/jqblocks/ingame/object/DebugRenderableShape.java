package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 17.07.12
 */
public class DebugRenderableShape implements RenderableObject<DebugRenderableShape>{

  Shape shape;
  Color color;

  public DebugRenderableShape(Shape shape, Color color) {
    this.shape = shape;
    this.color = color;
  }

  @Override
  public BeanName<? extends ObjectRenderer<DebugRenderableShape>> getRenderer() {
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
