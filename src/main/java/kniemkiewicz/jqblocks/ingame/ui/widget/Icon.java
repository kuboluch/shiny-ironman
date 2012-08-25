package kniemkiewicz.jqblocks.ingame.ui.widget;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Texture;
import kniemkiewicz.jqblocks.ingame.ui.renderer.TwlImage;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

/**
 * User: qba
 * Date: 07.08.12
 */
public class Icon extends Widget {

  Image image;
  int width;
  int height;

  public Icon(Image image, int width, int height) {
    assert image != null;
    assert width > 0;
    assert height > 0;
    this.image = image;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getPreferredInnerWidth() {
    return width;
  }

  @Override
  public int getPreferredInnerHeight() {
    return height;
  }

  @Override
  protected void paintWidget(GUI gui) {
    // Important! Clears texture bind cache to avoid twl-slick image rendering problem
    TextureImpl.unbind();
    image.draw(getInnerX(), getInnerY(), width, height);
  }
}
