package kniemkiewicz.jqblocks.ingame.ui;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * User: qba
 * Date: 06.08.12
 */
public class ImageUtils {

  public static Image loadImage(GUI gui, String imagePath) {
    try {
      InputStream in = ImageUtils.class.getResourceAsStream(imagePath);
      try {
        PNGDecoder decoder = new PNGDecoder(in);

        System.out.println("width=" + decoder.getWidth());
        System.out.println("height=" + decoder.getHeight());

        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        DynamicImage image = gui.getRenderer().createDynamicImage(decoder.getWidth(), decoder.getHeight());
        image.update(buf, DynamicImage.Format.RGBA);
        return image;
      } finally {
        in.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load ui image", e);
    }
  }

  public static int getImageWidth(String imagePath) {
    try {
    InputStream in = ImageUtils.class.getResourceAsStream(imagePath);
      try {
        PNGDecoder decoder = new PNGDecoder(in);
        return decoder.getWidth();
      } finally {
        in.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load ui image", e);
    }
  }

  public static int getImageHeight(String imagePath) {
    try {
    InputStream in = ImageUtils.class.getResourceAsStream(imagePath);
      try {
        PNGDecoder decoder = new PNGDecoder(in);
        return decoder.getHeight();
      } finally {
        in.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load ui image", e);
    }
  }
}
