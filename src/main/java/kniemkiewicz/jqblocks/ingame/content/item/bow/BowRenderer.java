package kniemkiewicz.jqblocks.ingame.content.item.bow;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.ControllerUtils;
import kniemkiewicz.jqblocks.ingame.item.renderer.EquippedItemRenderer;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.GraphicsContainer;
import kniemkiewicz.jqblocks.util.Pair;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Vector;

/**
 * User: knie
 * Date: 8/25/12
 */
@Component
public final class BowRenderer implements ItemRenderer<BowItem>, EquippedItemRenderer<BowItem> {

  public static Color ARROW_COLOR = new Color(100.0f / 255, 50.0f / 255, 0);

  @Autowired
  PointOfView pointOfView;

  @Autowired
  BowItemController bowItemController;

  @Autowired
  GraphicsContainer graphicsContainer;

  @Autowired
  ControllerUtils controllerUtils;

  @Override
  public void renderItem(BowItem item, int x, int y, int square_size, boolean drawFlipped) {
    // TODO: Some numbers below work well for current square size and not for others.
    float x1 = x + square_size * 0.1f;
    float y1 = y + square_size * 0.1f;
    float x2 = x + square_size * 0.9f;
    float y2 = y + square_size * 0.9f;
    Graphics g = graphicsContainer.getGraphics();
    g.setLineWidth(2);
    g.setColor(Color.black);
    g.drawArc(x1 - 1, y1 - 1, square_size * 0.8f + 2, square_size * 0.8f + 2, 10, -45, 135);
    g.setColor(new Color(50, 50, 50));
    g.drawLine(x2, y1, x1, y2);
    g.setColor(ARROW_COLOR);
    g.drawLine(x1 + 5, y1 + 5, x2 + 1, y2 + 1);
    g.setLineWidth(1);
  }

  @Override
  public void renderEquippedItem(BowItem item, Graphics g) {
    Vector2f pos = bowItemController.getScreenBowPosition();
    float x0 = pos.getX();
    float y0 = pos.getY();
    float r1 = Sizes.BLOCK * 1.5f;
    Vector2f dr = controllerUtils.getCurrentDirection(r1, pos);
    float dx = dr.getX();
    float dy = dr.getY();
    g.setColor(Color.gray);
    g.drawLine(x0, y0, x0 + dx * 40, y0 + dy * 40);
    g.setColor(ARROW_COLOR);
    g.setLineWidth(2);
    g.drawLine(x0 + dx / 6, y0 + dy / 6, x0 + dx * 1.2f, y0 + dy * 1.2f);
    g.setColor(Color.black);
    float arcRadius = Sizes.BLOCK;
    float arcCenterX = x0 + dx / 3;
    float arcCenterY = y0 + dy / 3;
    float angle = (float) (Math.acos(dy / r1) * 180 / Math.PI);
    if (dx > 0) {
      angle = 360 - angle;
    }
    g.drawArc(arcCenterX - arcRadius, arcCenterY - arcRadius, arcRadius * 2, arcRadius * 2, angle, angle + 180);
    g.setLineWidth(1);
    {
      float arcDx = dx / r1 * arcRadius;
      float arcDy = dy / r1 * arcRadius;
      g.drawLine(arcCenterX - arcDy, arcCenterY + arcDx, arcCenterX + arcDy, arcCenterY - arcDx);
    }
  }
}
