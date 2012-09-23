package kniemkiewicz.jqblocks.ingame;

import kniemkiewicz.jqblocks.Configuration;
import kniemkiewicz.jqblocks.Main;
import kniemkiewicz.jqblocks.ingame.controller.event.EventBus;
import kniemkiewicz.jqblocks.ingame.controller.event.screen.ScreenMovedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * User: krzysiek
 * Date: 15.07.12
 */
@Component
public final class PointOfView {
  int shiftX;
  int shiftY;

  int windowWidth;
  int windowHeight;

  @Autowired
  Configuration configuration;

  @Autowired
  EventBus eventBus;

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

  public int getWindowWidth() {
    return windowWidth;
  }

  public int getWindowHeight() {
    return windowHeight;
  }

  public void setCenter(int x, int y) {
    int oldShiftX = shiftX;
    int oldShiftY = shiftY;
    shiftX = x - windowWidth / 2;
    shiftY = y - windowHeight / 2;
    if (oldShiftX != shiftX || oldShiftY != shiftY) {
      eventBus.broadcast(new ScreenMovedEvent(oldShiftX, oldShiftY, shiftX, shiftY));
    }
  }
}
