package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public interface RenderableObject {

  enum Layer {
    MINUS_INF,
    BACKGROUND,
    WALL,
    OBJECTS,
    ARROWS,
    PLUS_INF
  }

  // Graphics are shifted by pov before a call to this method.
  public void renderObject(Graphics g, PointOfView pov);

  public Shape getShape();

  public Layer getLayer();
}
