package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 15.08.12
 */
public class Ladder implements RenderableObject<Ladder>, PickableObject, DroppableObject<Ladder> {

  Rectangle rectangle;

  public Ladder(int x, int y) {
    this.rectangle = new Rectangle(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y), Sizes.BLOCK * 2, Sizes.BLOCK * 2);
  }

  @Override
  public void setYAndUpdate(float y) {
    rectangle.setY(y);
  }

  @Override
  public Item getItem() {
    return LadderItemFactory.getItem();
  }

  @Override
  public PickableObjectType getType() {
    return PickableObjectType.ACTION;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRenderer.class, "ladderRenderer");

  @Override
  public BeanName<? extends ObjectRenderer<? super Ladder>> getRenderer() {
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
}
