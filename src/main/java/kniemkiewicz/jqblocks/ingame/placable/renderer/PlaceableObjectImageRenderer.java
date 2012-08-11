package kniemkiewicz.jqblocks.ingame.placable.renderer;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.workplace.PlaceableWorkplaceObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 10.08.12
 */
public class PlaceableObjectImageRenderer implements ObjectRenderer<PlaceableWorkplaceObject> {

  ImageRenderer baseRenderer;

  public PlaceableObjectImageRenderer(ImageRenderer baseRenderer) {
    this.baseRenderer = baseRenderer;
  }

  @Override
  public void render(PlaceableWorkplaceObject object, Graphics g, PointOfView pov) {
    Shape shape = object.getShape();
    Image image = baseRenderer.getImage();
    if (object.canBePlaced()) {
      baseRenderer.getImage().draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), Color.green);
    } else {
      baseRenderer.getImage().draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), Color.red);
    }
  }
}
