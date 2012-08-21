package kniemkiewicz.jqblocks.util;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * User: krzysiek
 * Date: 20.08.12
 */
public class FlippingImage extends Image {

  public FlippingImage(String ref) throws SlickException {
    super(ref);
  }

  private boolean flipNext = false;

  public void flipNext() {
    this.flipNext = true;
  }

  // Who said you should not override implementations...
  public void drawEmbedded(float x,float y,float width,float height) {
    if (flipNext) {
      textureOffsetX += textureWidth;
      textureWidth = - textureWidth;
    }
    super.drawEmbedded(x, y, width, height);
    if (flipNext) {
      textureOffsetX += textureWidth;
      textureWidth = - textureWidth;
    }
    flipNext = false;
  }
}
