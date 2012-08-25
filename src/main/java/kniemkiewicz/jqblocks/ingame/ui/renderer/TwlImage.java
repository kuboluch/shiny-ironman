package kniemkiewicz.jqblocks.ingame.ui.renderer;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.ui.ImageUtils;
import kniemkiewicz.jqblocks.ingame.ui.Initializable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 06.08.12
 */
public class TwlImage implements de.matthiasmann.twl.renderer.Image, Initializable {

  private boolean initialized = false;

  private String imagePath;

  private de.matthiasmann.twl.renderer.Image image;

  private int imageWidth = -1;
  private int imageHeight = -1;

  public TwlImage(String imagePath) {
    this.imagePath = imagePath;
  }

  public void init(GUI gui) {
    if (!initialized) {
      image = ImageUtils.loadImage(gui, imagePath);
      initialized = true;
    }
  }

  @Override
  public int getWidth() {
    if (!initialized) {
      if (imageWidth < 0) {
        imageWidth = ImageUtils.getImageWidth(imagePath);
      }
      return imageWidth;
    }
    return image.getWidth();
  }

  @Override
  public int getHeight() {
    if (!initialized) {
      if (imageHeight < 0) {
        imageHeight = ImageUtils.getImageHeight(imagePath);
      }
      return imageHeight;
    }
    return image.getHeight();
  }

  @Override
  public void draw(AnimationState as, int x, int y) {
    assert initialized;
    image.draw(as, x, y);

  }

  @Override
  public void draw(AnimationState as, int x, int y, int width, int height) {
    assert initialized;
    image.draw(as, x, y, width, height);
  }

  @Override
  public de.matthiasmann.twl.renderer.Image createTintedVersion(Color color) {
    assert initialized;
    return createTintedVersion(color);
  }
}
