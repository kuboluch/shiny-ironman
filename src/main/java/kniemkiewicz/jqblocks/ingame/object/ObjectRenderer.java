package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.BeanNameAware;

/**
 * User: knie
 * Date: 7/24/12
 */
public interface ObjectRenderer<T extends RenderableObject> {
  // Graphics are shifted by pov before a call to this method.
  void render(T object, Graphics g, PointOfView pov);
}
