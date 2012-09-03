package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class ProgressBar implements RenderableObject<ProgressBar> {

  final Rectangle rectangle;
  float percentage;

  public ProgressBar(Rectangle rect) {
    rectangle = rect;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  public static void render(Graphics g, float x, float y, float width, float height, float percentage) {
    if (percentage > 1) {
      percentage = 1;
    }
    float r = height / 2;
    g.setColor(Color.green);
    g.fillArc(x, y, 2 * r, 2 * r + 1, 90, 270);
    if (percentage <= 0.1f) {
      g.setColor(Color.red);
      float d = r * percentage / 0.1f;
      g.fillArc(x + d, y, 2 * r - d, 2 * r + 1, 90, 270);
      g.fillRect(x + r, y, width - 2 * r, height);
    }
    if (percentage > 0.1f && percentage < 0.9f) {
      float w = (width  - 2 * r) * (percentage - 0.1f) / 0.8f;
      g.setColor(Color.green);
      g.fillRect(x + r, y, w, height);
      g.setColor(Color.red);
      g.fillRect(x + r + w, y, width - 2 * r - w, height);
    }
    g.setColor(Color.red);
    g.fillArc(x + width - 2 * r, y, 2 * r, 2 * r, 270, 90);
    if (percentage >= 0.9f) {
      float d = r * (percentage - 0.9f) / 0.1f;
      g.setColor(Color.green);
      g.fillArc(x + width - 2 * r, y, r + d, 2 * r, 270, 90);
      g.fillRect(x + r, y, width - 2 * r, height);
    }
    g.setColor(Color.black);
    g.drawArc(x, y, 2 * r, 2 * r + 1, 90, 270);
    g.drawLine(x + r, y, x + width - r, y);
    g.drawLine(x + r, y + height, x + width - r, y + height);
    g.drawArc(x + width - 2 * r, y, 2 * r, 2 * r, 270, 90);
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    render(g, rectangle.getX(), rectangle.getY() + 0.1f * rectangle.getHeight(), rectangle.getWidth(), 0.1f * rectangle.getHeight(), percentage);
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
