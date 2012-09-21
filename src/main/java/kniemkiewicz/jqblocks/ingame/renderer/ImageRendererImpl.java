package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.renderer.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/29/12
 */
// This class has to be explicitly defined in xml per each item, to allow
// setting different images.
public class ImageRendererImpl<T extends RenderableObject> implements ItemRenderer<Item>, ImageRenderer<T> {

  Image image;
  Image flippedImage;

  public ImageRendererImpl() {
  }

  public ImageRendererImpl(String imagePath) {
    try {
      this.image = new Image(imagePath);
    } catch (SlickException e) {
      throw new RuntimeException("Failed to load image", e);
    }
  }

  public ImageRendererImpl(Image image) {
    this.image = image;
  }

  public ImageRendererImpl(XMLPackedSheet sheet, String imageName) {
    this.image = sheet.getSprite(imageName);
  }

  @Override
  public Image getImage() {
    return image;
  }

  private Image getFlippedImage() {
    if (flippedImage == null) {
      flippedImage = image.getFlippedCopy(true, false);
    }
    return flippedImage;
  }

  @Override
  public void renderItem(Item item, int x, int y, int square_size, boolean drawFlipped) {
    if (drawFlipped) {
      getFlippedImage().draw(x, y, square_size, square_size);
    } else {
      image.draw(x, y, square_size, square_size);
    }
  }

  @Override
  public void render(T object, Graphics g, PointOfView pov) {
    Shape shape = object.getShape();
    image.draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    g.setColor(Color.black);
    g.drawRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
  }
}
