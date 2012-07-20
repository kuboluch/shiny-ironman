package kniemkiewicz.jqblocks.ingame;

import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class PointOfView {
  int shiftX;
  int shiftY;

  public int getShiftX() {
    return shiftX;
  }

  public void setShiftX(int shiftX) {
    this.shiftX = shiftX;
  }

  public int getShiftY() {
    return shiftY;
  }

  public void setShiftY(int shiftY) {
    this.shiftY = shiftY;
  }

  public void setCenterX(int x) {
    shiftX = x - Sizes.WINDOW_WIDTH / 2;
  }

  public void setCenterY(int y) {
    shiftY = y - Sizes.WINDOW_HEIGHT / 2;
  }
}
