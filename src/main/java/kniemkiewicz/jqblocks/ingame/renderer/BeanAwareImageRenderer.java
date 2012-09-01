package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.springframework.beans.factory.BeanNameAware;

/**
 * User: qba
 * Date: 01.09.12
 */
public interface BeanAwareImageRenderer<T extends RenderableObject> extends ImageRenderer<T>, BeanNameAware {

  String getBeanName();
}
