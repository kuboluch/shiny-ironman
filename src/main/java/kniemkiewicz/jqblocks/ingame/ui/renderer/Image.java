package kniemkiewicz.jqblocks.ingame.ui.renderer;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.AnimationState;
import kniemkiewicz.jqblocks.ingame.ui.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * User: qba
 * Date: 06.08.12
 */
public class Image implements de.matthiasmann.twl.renderer.Image {

  @Autowired
  GUI gui;

  private String imagePath;

  private de.matthiasmann.twl.renderer.Image image;

  public Image(String imagePath) {
    this.imagePath = imagePath;
  }

  @PostConstruct
  void init() {
    image = ImageUtils.loadImage(gui, imagePath);
  }

  @Override
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int getHeight() {
    return image.getHeight();
  }

  @Override
  public void draw(AnimationState as, int x, int y) {
    image.draw(as, x, y);

  }

  @Override
  public void draw(AnimationState as, int x, int y, int width, int height) {
    image.draw(as, x, y, width, height);
  }

  @Override
  public de.matthiasmann.twl.renderer.Image createTintedVersion(Color color) {
    return createTintedVersion(color);
  }
}
