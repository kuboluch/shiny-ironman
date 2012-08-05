package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.*;

/**
 * User: qba
 * Date: 05.08.12
 */
public class Workplace {
  private String name;
  private int blockWidth;
  private int blockHeight;
  private ImageRenderer renderer;

  public Workplace(String name, ImageRenderer renderer, int blockWidth, int blockHeight) {
    this.name = name;
    this.renderer = renderer;
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;
  }

  public String getName() {
    return name;
  }

  public Image getIcon() {
    return renderer.getImage();
  }

  public WorkplaceBackgroundElement getPlaceableObject(int x, int y) {
    BeanName rendererBeanName = new BeanName(renderer.getClass(), renderer.getBeanName());
    return new WorkplaceBackgroundElement(x, y, blockWidth * Sizes.BLOCK, blockHeight * Sizes.BLOCK, rendererBeanName);
  }
}
