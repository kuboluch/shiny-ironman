package kniemkiewicz.jqblocks.ingame.object;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;

/**
 * User: krzysiek
 * Date: 08.07.12
 */
// T should be equal to class implementing this interface. Unfortunately Java does not
// allow me to force it at compile time.
public interface RenderableObject<T extends RenderableObject> extends PhysicalObject {

  enum Layer {
    MINUS_INF,
    BACKGROUND,
    PASSIVE_OBJECTS,
    WALL,
    OBJECTS,
    PICKABLE_OBJECTS,
    ARROWS,
    PLUS_INF
  }

  // This should return class implementing ObjectRenderer or null. Null means
  // that this object will handle rendering itself, using renderObject method.
  // Note that AFAIK all "?" in templates around this interface are required for
  // our code to compile. Enjoy!
  BeanName<? extends ObjectRenderer<? super T>> getRenderer();

  // Graphics are shifted by pov before a call to this method.
  public void renderObject(Graphics g, PointOfView pov);

  public Layer getLayer();
}
