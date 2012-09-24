package kniemkiewicz.jqblocks.ingame.content.background;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.SimpleImageRenderer;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.geom.Vector2f;

import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 21.09.12
 */
public class Portal extends AbstractBackgroundElement {

  public static class Destination implements Serializable{
    final private Vector2f pos;

    public Destination(Vector2f pos) {
      this.pos = pos;
    }

    public Vector2f getPos() {
      return pos;
    }
  }

  private static final long serialVersionUID = 1;

  public static int HEIGHT = Sizes.BLOCK * 5;
  public static int WIDTH = Sizes.BLOCK * 4;

  final private Destination destination;

  public Portal(int x, int y, Destination destination) {
    super(x, y, WIDTH, HEIGHT);
    this.destination = destination;
  }

  @Override
  public boolean isResource() {
    return true;
  }

  @Override
  public boolean requiresFoundation() {
    return true;
  }

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(SimpleImageRenderer.class, "portalRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  public Destination getDestination() {
    return destination;
  }
}
