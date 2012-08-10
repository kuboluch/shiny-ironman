package kniemkiewicz.jqblocks.ingame.placable.renderer;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 10.08.12
 */
public class PlaceableObjectImageRenderer implements ObjectRenderer<RenderableObject> {

  ImageRenderer baseRenderer;

  public PlaceableObjectImageRenderer(ImageRenderer baseRenderer) {
    this.baseRenderer = baseRenderer;
  }

  @Override
  public void render(RenderableObject object, Graphics g, PointOfView pov) {
    Shape shape = object.getShape();
    baseRenderer.getImage().draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
  }
}
