package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceBackgroundElement extends AbstractBackgroundElement<WorkplaceBackgroundElement> {

  private BeanName rendererBeanName;

  public WorkplaceBackgroundElement(int x, int y, int width, int height, BeanName rendererBeanName) {
    super(x, y, width, height);
    this.rendererBeanName = rendererBeanName;
  }

  @Override
  public BeanName<ImageRenderer> getRenderer() {
    return new BeanName(rendererBeanName.getClazz(), rendererBeanName.getName());
  }

  @Override
  public Layer getLayer() {
    return Layer.PASSIVE_OBJECTS;
  }
}
