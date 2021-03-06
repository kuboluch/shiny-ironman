package kniemkiewicz.jqblocks.ingame.block;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.block.renderer.DirtBlockTypeRenderer;
import kniemkiewicz.jqblocks.ingame.controller.InputListener;
import kniemkiewicz.jqblocks.ingame.controller.KeyboardUtils;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.renderer.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: krzysiek
 * Date: 12.08.12
 */
@Component
public class MapView implements InputListener, Renderable {

  int framesLeft = 0;

  @Autowired
  SolidBlocks solidBlocks;

  @Autowired
  PointOfView pointOfView;

  @Override
  public void listen(Input input, int delta) {
    if (KeyboardUtils.isMapViewPressed(input)) {
      // Turning on map view disables update() as render becomes too slow. Max frames left allows game to return to
      // normal state after some time.
      framesLeft = 30;
    }
  }

  @Override
  public void render(Graphics g) {
    if (framesLeft > 0) {
      g.setColor(Color.black);
      g.fillRect(0, 0, pointOfView.getWindowWidth(), pointOfView.getWindowHeight());
      RawEnumTable<WallBlockType> table = solidBlocks.getBlocks();
      int scaleX = (int)Math.ceil(1.f * table.getWidth() / pointOfView.getWindowWidth());
      int scaleY = (int)Math.ceil(1.f * table.getHeight() / pointOfView.getWindowHeight());
      for (int i = 0; i < table.getWidth() / scaleX; i++) {
        for (int j = 0; j < table.getHeight() / scaleY; j++) {
          WallBlockType w = table.get(i * scaleX,j * scaleY);
          switch (w) {
            case DIRT:
              g.setColor(DirtBlockTypeRenderer.BROWN);
              break;
            case ROCK:
              g.setColor(Color.gray);
              break;
            case EMPTY:
              g.setColor(RenderQueue.SKY);
              break;
            case MAGIC_BRICK_WALL:
              g.setColor(Color.pink);
              break;
            default:
              throw new IllegalArgumentException("Unknown WallBlockType :" + w);
          }
          g.drawRect(i,j,1,1);
        }
      }
      framesLeft -= 1;
    }
  }

  @Override
  public boolean isDisposable() {
    return false;
  }
}
