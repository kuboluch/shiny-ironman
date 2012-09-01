package kniemkiewicz.jqblocks.ingame.content.item.torch;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 29.08.12
 */
public class Torch implements RenderableObject<Torch>, PickableObject, DroppableObject<Torch> {

  Rectangle rectangle;

  public Torch(int x, int y) {
    this.rectangle = new Rectangle(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y), TorchDefinition.WIDTH, TorchDefinition.HEIGHT);
  }

  @Override
  public void setYAndUpdate(float y) {
    rectangle.setY(y);
  }

  @Override
  public Item getItem() {
    return TorchItemFactory.getItem();
  }

  @Override
  public PickableObjectType getType() {
    return PickableObjectType.ACTION;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return TorchDefinition.RENDERER;
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
}
