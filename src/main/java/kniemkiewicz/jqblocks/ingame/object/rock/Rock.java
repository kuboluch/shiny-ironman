package kniemkiewicz.jqblocks.ingame.object.rock;

import kniemkiewicz.jqblocks.ingame.MovingObjects;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.RenderQueue;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.MovingPhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PickableObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

/**
 * Rocks are mostly a test subject to check if picking up objects works.
 * User: knie
 * Date: 7/25/12
 */
public class Rock implements RenderableObject<Rock>, PickableObject, MovingPhysicalObject {

  private static final long serialVersionUID = 1;

  static int SMALL_CIRCLE_RADIUS = Sizes.BLOCK / 4;
  static int LARGE_CIRCLE_RADIUS = Sizes.BLOCK / 2;

  static Color LARGE_COLOR = Color.gray;
  static Color SMALL_COLOR = Color.lightGray;

  //TODO: this will not load.
  transient Circle circle;
  boolean large;

  public Rock(int centerX, int maxY, boolean large) {
    this.large = large;
    int radius = large ? LARGE_CIRCLE_RADIUS : SMALL_CIRCLE_RADIUS;
    circle = new Circle(centerX, maxY - radius, radius);
  }

  public boolean addTo(RenderQueue renderQueue, MovingObjects movingObjects) {
    if (!movingObjects.add(this)) return false;
    renderQueue.add(this);
    return true;
  }

  @Override
  public Class<? extends ObjectRenderer<Rock>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(large ? LARGE_COLOR : SMALL_COLOR);
    g.fill(circle);
    g.setColor(Color.black);
    g.draw(circle);
  }

  @Override
  public Layer getLayer() {
    return Layer.PASSIVE_OBJECTS;
  }

  @Override
  public Shape getShape() {
    return circle;
  }

  @Override
  public Item getItem() {
    return new RockItem(large);
  }

  @Override
  public void setY(int y) {
    circle.setY(y);
  }
}
