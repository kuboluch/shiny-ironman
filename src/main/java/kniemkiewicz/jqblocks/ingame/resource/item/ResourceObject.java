package kniemkiewicz.jqblocks.ingame.resource.item;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.inventory.item.Item;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.ingame.resource.Resource;
import kniemkiewicz.jqblocks.ingame.resource.ResourceType;
import kniemkiewicz.jqblocks.ingame.resource.renderer.ResourceRenderer;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/27/12
 */
public class ResourceObject<T extends Resource>
    implements RenderableObject<ResourceObject>, PickableObject, DroppableObject<ResourceObject> {

  private static final long serialVersionUID = 1;

  public static final int SIZE = Sizes.BLOCK;

  Rectangle rectangle;

  T resource;

  public ResourceObject(T resource, int x, int y) {
    this.rectangle = ResourceObject.getShape(x, y);
    this.resource = resource;
  }

  public static Rectangle getShape(int x, int y) {
    return new Rectangle(x, y, SIZE, SIZE);
  }

  private static final BeanName<ResourceRenderer> RENDERER = new BeanName<ResourceRenderer>(ResourceRenderer.class, "resourceRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.PICKABLE_OBJECTS;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  @Override
  public Item getItem() {
    return new SimpleResourceItem(resource);
  }

  @Override
  public PickableObjectType getType() {
    return PickableObjectType.RESOURCE;
  }

  @Override
  public void setYAndUpdate(float y) {
    rectangle.setY(y);
  }

  public int getResourceAmount() {
    return resource.getAmount();
  }

  public boolean isFull() {
    assert resource.getAmount() <= resource.getMaxPileSize();
    return resource.getAmount() == resource.getMaxPileSize();
  }

  public int getResourceMaxPileSize() {
    return resource.getMaxPileSize();
  }

  public ResourceType getResourceType() {
    return resource.getType();
  }

  public T addResource(T resource) {
    if (!isFull()) {
      int roomLeft = this.resource.getMaxPileSize() - this.resource.getAmount();
      if (roomLeft < resource.getAmount()) {
        resource.transferTo(this.resource, roomLeft);
      } else {
        resource.transferTo(this.resource);
      }
    }
    return resource;
  }

  public boolean isSameType(Resource resource) {
    Assert.assertTrue(resource != null);
    return this.resource.getType().equals(resource.getType());
  }
}
