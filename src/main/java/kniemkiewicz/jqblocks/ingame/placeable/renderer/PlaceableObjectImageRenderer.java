package kniemkiewicz.jqblocks.ingame.placeable.renderer;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.workplace.PlaceableWorkplaceObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 10.08.12
 */
public class PlaceableObjectImageRenderer implements ObjectRenderer<PlaceableWorkplaceObject> {

  private static final Color validColor = new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 150);
  private static final Color invalidColor = new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 150);

  ImageRenderer baseRenderer;

  public PlaceableObjectImageRenderer(ImageRenderer baseRenderer) {
    this.baseRenderer = baseRenderer;
  }

  @Override
  public void render(PlaceableWorkplaceObject object, Graphics g, PointOfView pov) {
    Shape shape = object.getShape();
    if (object.canBePlaced()) {
      baseRenderer.getImage().draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), validColor);
    } else {
      baseRenderer.getImage().draw(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), invalidColor);
    }
  }
}
