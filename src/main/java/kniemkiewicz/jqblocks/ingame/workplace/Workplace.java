package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.ingame.ui.renderer.Image;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 05.08.12
 */
public class Workplace {
  private String name;
  private int blockWidth;
  private int blockHeight;
  private ImageRenderer renderer;
  private Image uiImage;

  public Workplace(String name, int blockWidth, int blockHeight, ImageRenderer renderer, Image uiImage) {
    this.name = name;
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;
    this.renderer = renderer;
    this.uiImage = uiImage;
  }

  public String getName() {
    return name;
  }

  public Image getUIImage() {
    return uiImage;
  }

  public WorkplaceBackgroundElement getPlaceableObject(int x, int y) {
    BeanName rendererBeanName = new BeanName(renderer.getClass(), renderer.getBeanName());
    return new WorkplaceBackgroundElement(x, y, blockWidth * Sizes.BLOCK, blockHeight * Sizes.BLOCK, rendererBeanName);
  }
}
