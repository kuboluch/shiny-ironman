package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
public interface RenderableObject {
  // Graphics are shifted by pov before a call to this method.
  public void renderObject(Graphics g, PointOfView pov);

  public Shape getShape();
}
