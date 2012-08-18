package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;

import javax.annotation.Resource;

/**
 * User: krzysiek
 * Date: 18.08.12
 */
public class TwoFacedImageRenderer implements ObjectRenderer<TwoFacedImageRenderer.Renderable> {

  public interface Renderable extends RenderableObject<Renderable> {
    boolean isLeftFaced();
  }

  final private Image leftImage;
  final private Image rightImage;
  final private int imageWidth;

  public TwoFacedImageRenderer(Image leftImage, Image rightImage, int imageWidth) {
    this.leftImage = leftImage;
    this.rightImage = rightImage;
    this.imageWidth = imageWidth;
  }

  @Override
  public void render(Renderable p, Graphics g, PointOfView pov) {
    Shape shape = p.getShape();
    if (p.isLeftFaced()) {
      leftImage.draw((int)shape.getMinX(), (int)shape.getMinY(), imageWidth, shape.getHeight());
    } else {
      rightImage.draw((int)shape.getMaxX() - imageWidth, (int)shape.getMinY(), imageWidth, shape.getHeight());
    }
  }
}

