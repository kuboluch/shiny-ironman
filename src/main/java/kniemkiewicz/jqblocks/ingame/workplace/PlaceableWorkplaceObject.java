package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.object.background.BackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.placeable.Placeable;
import kniemkiewicz.jqblocks.ingame.placeable.renderer.PlaceableObjectImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * User: qba
 * Date: 09.08.12
 */
public class PlaceableWorkplaceObject implements RenderableObject<WorkplaceBackgroundElement>, Placeable {

  WorkplaceBackgroundElement backgroundElement;

  ObjectRenderer renderer;

  WorkplaceController controller;

  public PlaceableWorkplaceObject(Workplace workplace, int x, int y, ImageRenderer renderer, WorkplaceController controller) {
    this.renderer = new PlaceableObjectImageRenderer(renderer);
    this.controller = controller;
    BeanName rendererBeanName = new BeanName(renderer.getClass(), renderer.getBeanName());
    backgroundElement = new WorkplaceBackgroundElement(workplace, x, y, rendererBeanName);

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
  public BeanName<? extends ObjectRenderer<? super WorkplaceBackgroundElement>> getRenderer() {
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
    return controller.canBePlaced();
  }
}
