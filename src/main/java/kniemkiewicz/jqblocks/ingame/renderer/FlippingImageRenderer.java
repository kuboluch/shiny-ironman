package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 23.09.12
 */
public class FlippingImageRenderer<T extends RenderableObject & HasFullXYMovement> extends SimpleImageRenderer<T> {

  public FlippingImageRenderer() {
  }

  public FlippingImageRenderer(String imagePath) {
    super(imagePath);
  }

  public FlippingImageRenderer(Image image) {
    super(image);
  }

  public FlippingImageRenderer(XMLPackedSheet sheet, String imageName) {
    super(sheet, imageName);
  }

  public FlippingImageRenderer(SpriteSheet spritesheet, int x, int y) {
    super(spritesheet, x, y);
  }

  @Override
  public void render(T object, Graphics g, PointOfView pov) {
    Image image = this.image;
    if (object.getXYMovement().getXMovement().getDirection()) {
      image = getFlippedImage();
    }
    Shape shape = object.getShape();
    image.draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    if (DRAW_SHAPES_BOUNDARIES) {
      g.setColor(Color.black);
      g.drawRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }
  }
}
