package kniemkiewicz.jqblocks.ingame.object.peon;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.PhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.TwoFacedImageRenderer;
import kniemkiewicz.jqblocks.ingame.object.hp.HasHealthPoints;
import kniemkiewicz.jqblocks.ingame.object.hp.HealthPoints;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 19.08.12
 */
public class Peon implements PhysicalObject,HasHealthPoints, TwoFacedImageRenderer.Renderable{
  @Override
  public HealthPoints getHp() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public BeanName getHealthController() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isLeftFaced() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public BeanName<? extends ObjectRenderer<? super TwoFacedImageRenderer.Renderable>> getRenderer() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Layer getLayer() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Shape getShape() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
