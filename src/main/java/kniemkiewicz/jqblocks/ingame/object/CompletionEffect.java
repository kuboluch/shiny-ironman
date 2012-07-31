package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class CompletionEffect implements RenderableObject<CompletionEffect> {

  final Rectangle rectangle;
  float percentage;

  public CompletionEffect(Rectangle rect) {
    rectangle = rect;
  }

  @Override
  public BeanName<? extends ObjectRenderer<CompletionEffect>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    float height = rectangle.getHeight() * percentage / 2;
    float width = rectangle.getWidth() * percentage / 2;
    g.setColor(Color.black);
    g.setLineWidth(2);
    g.drawLine(rectangle.getX(), rectangle.getCenterY() - height, rectangle.getX(), rectangle.getCenterY() + height);
    g.drawLine(rectangle.getMaxX() + 1, rectangle.getCenterY() - height, rectangle.getMaxX() + 1, rectangle.getCenterY() + height);
    g.drawLine(rectangle.getCenterX() - width, rectangle.getY(), rectangle.getCenterX() + width, rectangle.getY());
    g.drawLine(rectangle.getCenterX() - width, rectangle.getMaxY() + 1, rectangle.getCenterX() + width, rectangle.getMaxY() + 1);
    g.setLineWidth(1);
  }

  @Override
  public RenderableObject.Layer getLayer() {
    return RenderableObject.Layer.PLUS_INF;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  public void setPercentage(int percentage) {
    assert percentage >= 0;
    assert percentage <= 100;
    this.percentage = percentage / 100f;
  }
}
