package kniemkiewicz.jqblocks.ingame.content.transport.ladder;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.object.*;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 15.08.12
 */
public class Ladder implements RenderableObject<Ladder>, PickableObject, MovingPhysicalObject, DroppableObject<Ladder> {

  Rectangle rectangle;

  public Ladder(int x, int y) {
    this.rectangle = new Rectangle(Sizes.roundToBlockSizeX(x), Sizes.roundToBlockSizeY(y), Sizes.BLOCK * 2, Sizes.BLOCK * 2);
  }

  // Do not add objects manually. Using this method makes sure you won't forget any part.
  public boolean addTo(MovingObjects movingObjects, RenderQueue renderQueue) {
    if (!movingObjects.add(this)) return false;
    renderQueue.add(this);
    return true;
  }

  @Override
  public void setY(int y) {
    rectangle.setY(y);
  }

  @Override
  public Item getItem() {
    return new LadderItem();
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
