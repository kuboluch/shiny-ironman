package kniemkiewicz.jqblocks.ingame.workplace;

import kniemkiewicz.jqblocks.ingame.object.background.WorkplaceBackgroundElement;
import kniemkiewicz.jqblocks.util.BeanName;

/**
 * User: qba
 * Date: 09.08.12
 */
public class PlacableWorkplaceObject extends WorkplaceBackgroundElement {

  public PlacableWorkplaceObject(int x, int y, int width, int height, BeanName rendererBeanName) {
    super(x, y, width, height, rendererBeanName);
  }

  public void changeX(int x) {
    this.x = x;
    updateShape();
  }

  public void changeY(int y) {
    this.y = y;
    updateShape();
  }

  private void updateShape() {
    shape.setX(x);
    shape.setY(y);
  }
}
