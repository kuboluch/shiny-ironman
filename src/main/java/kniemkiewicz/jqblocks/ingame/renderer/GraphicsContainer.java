package kniemkiewicz.jqblocks.ingame.renderer;

import org.newdawn.slick.Graphics;
import org.springframework.stereotype.Component;

/**
 * User: qba
 * Date: 09.09.12
 */
@Component
public class GraphicsContainer {

  private Graphics graphics;

  public void setGraphics(Graphics graphics) {
    this.graphics = graphics;
  }

  public Graphics getGraphics() {
    return graphics;
  }
}
