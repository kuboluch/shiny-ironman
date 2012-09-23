package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.Placeable;
import kniemkiewicz.jqblocks.ingame.object.PlaceableObjectImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.BeanAwareImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializableBeanProxy;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 09.08.12
 */
public class PlaceableWorkplaceObject implements RenderableObject<WorkplaceBackgroundElement>, Placeable {

  WorkplaceBackgroundElement backgroundElement;

  PlaceableObjectImageRenderer renderer;

  SerializableBeanProxy<WorkplaceController> controller;

  public PlaceableWorkplaceObject(WorkplaceDefinition workplaceDefinition, int x, int y,
                                  BeanAwareImageRenderer renderer, WorkplaceController controller) {
    this.renderer = new PlaceableObjectImageRenderer(renderer);
    this.controller = SerializableBeanProxy.getInstance(controller);
    BeanName rendererBeanName = new BeanName(renderer.getClass(), renderer.getBeanName());
    backgroundElement = new WorkplaceBackgroundElement(workplaceDefinition, x, y, rendererBeanName);
  }

  public void changeX(int newX) {
    int x = Sizes.roundToBlockSizeX(newX);
    backgroundElement.getShape().setX(x);
  }

  public void changeY(int newY) {
    int y = Sizes.roundToBlockSizeX(newY);
    backgroundElement.getShape().setY(y);
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    renderer.render(this, g, pov);
  }

  @Override
  public Layer getLayer() {
    return Layer.OBJECTS;
  }

  @Override
  public Shape getShape() {
    return backgroundElement.getShape();
  }

  @Override
  public BackgroundElement getBackgroundElement() {
    return backgroundElement;
  }

  public boolean canBePlaced() {
    return controller.get().canBePlaced();
  }
}
