package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public class PointOfView {
  int shiftX;
  int shiftY;

  int windowWidth;
  int windowHeight;

  @Autowired
  Configuration configuration;

  @PostConstruct
  void init() {
    windowWidth = configuration.getInt(Main.WINDOW_WIDTH_NAME, Sizes.DEFAULT_WINDOW_WIDTH);
    windowHeight = configuration.getInt(Main.WINDOW_HEIGHT_NAME, Sizes.DEFAULT_WINDOW_HEIGHT);
  }

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
    shiftX = x - windowWidth / 2;
  }

  public void setCenterY(int y) {
    shiftY = y - windowHeight / 2;
  }

  public int getWindowWidth() {
    return windowWidth;
  }

  public int getWindowHeight() {
    return windowHeight;
  }
}
