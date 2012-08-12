package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.workplace.Workplace;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceBackgroundElement extends AbstractBackgroundElement<WorkplaceBackgroundElement> {

  private Workplace workplace;

  private BeanName rendererBeanName;

  public WorkplaceBackgroundElement(Workplace workplace, int x, int y, BeanName rendererBeanName) {
    super(x, y, workplace.getBlockWidth() * Sizes.BLOCK, workplace.getBlockHeight() * Sizes.BLOCK);
    this.workplace = workplace;
    this.rendererBeanName = rendererBeanName;
  }

  @Override
  public boolean isWorkplace() {
    return true;
  }

  @Override
  public BeanName<ImageRenderer> getRenderer() {
    return new BeanName(rendererBeanName.getClazz(), rendererBeanName.getName());
  }

  @Override
  public Layer getLayer() {
    return Layer.PASSIVE_OBJECTS;
  }

  public Workplace getWorkplace() {
    return workplace;
  }
}
