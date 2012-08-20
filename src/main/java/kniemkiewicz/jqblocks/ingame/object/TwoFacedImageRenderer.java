package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.FlippingImage;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 18.08.12
 */
public class TwoFacedImageRenderer implements ObjectRenderer<TwoFacedImageRenderer.Renderable> {

  public interface Renderable extends RenderableObject<Renderable> {
    boolean isLeftFaced();
  }

  final private FlippingImage leftImage;
  final private int imageWidth;

  public TwoFacedImageRenderer(FlippingImage leftImage, int imageWidth) {
    this.leftImage = leftImage;
    this.imageWidth = imageWidth;
  }

  public TwoFacedImageRenderer(FlippingImage leftImage) {
    this.leftImage = leftImage;
    this.imageWidth = -1;
  }

  @Override
  public void render(Renderable p, Graphics g, PointOfView pov) {
    Shape shape = p.getShape();
    if (p.isLeftFaced()) {
      float width = imageWidth;
      if (width < 0) {
        width = shape.getWidth();
      }
      leftImage.draw((int)shape.getMinX(), (int)shape.getMinY(), width, shape.getHeight());
    } else {
      leftImage.flipNext();
      if (imageWidth > 0) {
        leftImage.draw((int)shape.getMaxX() - imageWidth, (int)shape.getMinY(), imageWidth, shape.getHeight());
      } else {
        leftImage.draw((int)shape.getMinX(), (int)shape.getMinY(), shape.getWidth(), shape.getHeight());
      }
    }
  }
}

