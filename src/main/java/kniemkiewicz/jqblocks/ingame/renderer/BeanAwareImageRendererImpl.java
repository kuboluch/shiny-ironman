package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;

/**
 * User: qba
 * Date: 01.09.12
 */
public class BeanAwareImageRendererImpl<T extends RenderableObject> extends ImageRendererImpl<T> implements BeanAwareImageRenderer<T> {

  public String beanName;

  public BeanAwareImageRendererImpl(String imagePath) {
    super(imagePath);
  }

  public BeanAwareImageRendererImpl(Image image) {
    super(image);
  }

  public BeanAwareImageRendererImpl(XMLPackedSheet sheet, String imageName) {
    super(sheet, imageName);
  }

  @Override
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  @Override
  public String getBeanName() {
    return beanName;
  }
}
