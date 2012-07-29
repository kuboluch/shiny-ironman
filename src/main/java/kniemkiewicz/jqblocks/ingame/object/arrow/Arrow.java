package kniemkiewicz.jqblocks.ingame.object.arrow;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.item.BowItem;
import kniemkiewicz.jqblocks.ingame.util.LimitedSpeed;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/21/12
 */
public class Arrow implements RenderableObject<Arrow>,UpdateQueue.ToBeUpdated<Arrow> {

  private static final long serialVersionUID = 1;

  Line line;
  LimitedSpeed xMovement;
  LimitedSpeed yMovement;
  Object source;
  private static int LENGTH = Sizes.BLOCK;

  public Arrow(float x, float y, Object source, float xSpeed, float ySpeed) {
    this.line = new Line(x, y, x, y);
    xMovement = new LimitedSpeed(2 * xSpeed, xSpeed, x, 0);
    yMovement = new LimitedSpeed(Sizes.MAX_FALL_SPEED, ySpeed, y, 0);
    this.source = source;
  }

  @Override
  public Class<? extends ObjectRenderer<Arrow>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(BowItem.ARROW_COLOR);
    g.setLineWidth(2);
    g.draw(line);
    g.setLineWidth(1);
  }

  @Override
  public Shape getShape() {
    return line;
  }

  @Override
  public Layer getLayer() {
    return Layer.ARROWS;
  }


  public void update(int delta) {
    yMovement.setAcceleration(Sizes.G);
    xMovement.update(delta);
    yMovement.update(delta);
    float dx = xMovement.getSpeed();
    float dy = yMovement.getSpeed();
    float v = (float)Math.sqrt(dx * dx + dy * dy);
    assert v > 0;
    float lx = dx / v * LENGTH / 2;
    float ly = dy / v * LENGTH / 2;
    line.set(xMovement.getPos() - lx, yMovement.getPos() - ly, xMovement.getPos() + lx, yMovement.getPos() + ly);
  }

  public LimitedSpeed getXMovement() {
    return xMovement;
  }

  public LimitedSpeed getYMovement() {
    return yMovement;
  }

  public Object getSource() {
    return source;
  }

  @Override
  public Class<ArrowController> getUpdateController() {
    return ArrowController.class;
  }
}
