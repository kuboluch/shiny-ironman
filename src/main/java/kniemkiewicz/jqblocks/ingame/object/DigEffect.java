package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/24/12
 */
public class DigEffect implements RenderableObject<DigEffect> {

  private static final long serialVersionUID = 1;

  final Rectangle rectangle;
  final int startingEndurance;
  int currentEndurance;

  public DigEffect(int endurance, Rectangle rect) {
    startingEndurance = endurance;
    currentEndurance = startingEndurance;
    rectangle = rect;
  }

  @Override
  public Class<? extends ObjectRenderer<DigEffect>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    float percentage = 1 - 1.0f * currentEndurance / startingEndurance;
    float height = rectangle.getHeight() / 2 * percentage;
    float width = rectangle.getWidth() / 2 * percentage;
    g.setColor(Color.black);
    g.setLineWidth(2);
    g.drawLine(rectangle.getX(), rectangle.getCenterY() - height, rectangle.getX(), rectangle.getCenterY() + height);
    g.drawLine(rectangle.getMaxX() + 1, rectangle.getCenterY() - height, rectangle.getMaxX() + 1, rectangle.getCenterY() + height);
    g.drawLine(rectangle.getCenterX() - width, rectangle.getY(), rectangle.getCenterX() + width, rectangle.getY());
    g.drawLine(rectangle.getCenterX() - width, rectangle.getMaxY() + 1, rectangle.getCenterX() + width, rectangle.getMaxY() + 1);
    g.setLineWidth(1);
  }

  @Override
  public Layer getLayer() {
    return Layer.PLUS_INF;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  public void setCurrentEndurance(int currentEndurance) {
    this.currentEndurance = currentEndurance;
  }
}
