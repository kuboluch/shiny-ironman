package kniemkiewicz.jqblocks.ingame.object.bat;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: knie
 * Date: 7/24/12
 */
public class Bat implements RenderableObject<Bat>,UpdateQueue.ToBeUpdated<Bat> {


  @Override
  public Class<? extends ObjectRenderer<Bat>> getRenderer() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Shape getShape() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<Bat>> getController() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
