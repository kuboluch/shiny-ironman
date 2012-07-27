package kniemkiewicz.jqblocks.ingame.object.resource;

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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/27/12
 */
public class ResourceObject implements RenderableObject<ResourceObject>, PickableObject, MovingPhysicalObject {

  static final int SIZE = Sizes.BLOCK / 2;

  Rectangle rectangle;

  Resource resource;

  public ResourceObject(Resource resource, int x, int y) {
    this.rectangle = new Rectangle(x, y, SIZE, SIZE);
    this.resource = resource;
  }

  public void addTo(RenderQueue renderQueue, MovingObjects movingObjects) {
    renderQueue.add(this);
    Assert.executeAndAssert(movingObjects.addCollidingWithPlayer(this));
  }

  @Override
  public Class<? extends ObjectRenderer<ResourceObject>> getRenderer() {
    // TODO: something more fancy here, with dynamic images based on which resource we have inside.
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(Color.cyan);
    g.fill(rectangle);
  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  @Override
  public Item getItem() {
    return new ResourceItem(resource);
  }

  @Override
  public void setY(int y) {
    rectangle.setY(y);
  }
}
