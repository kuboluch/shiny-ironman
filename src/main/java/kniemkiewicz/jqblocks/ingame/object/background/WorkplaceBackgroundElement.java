package kniemkiewicz.jqblocks.ingame.object.background;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.workplace.WorkplaceDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializableBeanProxy;

/**
 * User: qba
 * Date: 05.08.12
 */
public class WorkplaceBackgroundElement extends AbstractBackgroundElement<WorkplaceBackgroundElement> {

  private SerializableBeanProxy<WorkplaceDefinition> workplace;

  private BeanName rendererBeanName;

  public WorkplaceBackgroundElement(WorkplaceDefinition workplaceDefinition, int x, int y, BeanName rendererBeanName) {
    super(x, y, workplaceDefinition.getBlockWidth() * Sizes.BLOCK, workplaceDefinition.getBlockHeight() * Sizes.BLOCK);
    this.workplace = SerializableBeanProxy.getInstance(workplaceDefinition);
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

  public WorkplaceDefinition getWorkplace() {
    return workplace.get();
  }
}
