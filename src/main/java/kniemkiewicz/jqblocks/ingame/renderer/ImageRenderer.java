package kniemkiewicz.jqblocks.ingame.renderer;

import de.matthiasmann.twl.GUI;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.item.Item;
import kniemkiewicz.jqblocks.ingame.item.ItemRenderer;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.FlippingImage;
import kniemkiewicz.jqblocks.ingame.ui.Initializable;
import kniemkiewicz.jqblocks.ingame.ui.renderer.TwlImage;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;
import org.springframework.beans.factory.BeanNameAware;

/**
 * User: knie
 * Date: 7/29/12
 */
// This class has to be explicitly defined in xml per each item, to allow
// setting different images.
public class ImageRenderer implements ItemRenderer<Item>, ObjectRenderer<RenderableObject>, BeanNameAware {

  public String beanName;

  FlippingImage image;

  public ImageRenderer(String imagePath) {
    try {
      this.image = new FlippingImage(imagePath);
    } catch (SlickException e) {
      throw new RuntimeException("Failed to load image", e);
    }
  }

  public ImageRenderer(FlippingImage image) {
    this.image = image;
  }

  public ImageRenderer(XMLPackedSheet sheet, String imageName) {
    this.image = new FlippingImage(sheet.getSprite(imageName));
  }

  public Image getImage() {
    return image;
  }

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size, boolean drawFlipped) {
    if (drawFlipped) {
      image.flipNext();
    }
    image.draw(x, y, square_size, square_size);
  }

  @Override
  public void render(RenderableObject object, Graphics g, PointOfView pov) {
    Shape shape = object.getShape();
    image.draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
  }

  public String getBeanName() {
    return beanName;
  }

  @Override
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }
}
