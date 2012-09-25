package kniemkiewicz.jqblocks.ingame.content.background;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.World;
import kniemkiewicz.jqblocks.ingame.content.item.arrow.StuckArrow;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.background.AbstractBackgroundElement;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRendererImpl;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.newdawn.slick.geom.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * User: krzysiek
 * Date: 21.09.12
 */
public class Portal extends AbstractBackgroundElement {

  public static class Destination implements Serializable{
    private transient Vector2f pos;

    public Destination(Vector2f pos) {
      this.pos = pos;
    }

    public Vector2f getPos() {
      return pos;
    }


    private void writeObject(ObjectOutputStream outputStream) throws IOException {
      //perform the default serialization for all non-transient, non-static fields
      outputStream.defaultWriteObject();
      SerializationUtils2.serializeVector2f(pos, outputStream);
    }


    // need to implement serialization as Circle is not Serializable
    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
      //always perform the default de-serialization first
      inputStream.defaultReadObject();
      pos = SerializationUtils2.deserializeVector2f(inputStream);
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

  private static final BeanName<ImageRenderer> RENDERER = new BeanName<ImageRenderer>(ImageRendererImpl.class, "portalRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  public Destination getDestination() {
    return destination;
  }
}
