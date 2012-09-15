package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.ImageIOImageData;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.velocity.SpringResourceLoader;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
public class GifAnimation implements ApplicationContextAware, Animation {

  String path;
  ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  class GifImageIOImageData extends ImageIOImageData {
    ByteBuffer byteBuffer;

    public ByteBuffer getImageBufferData() {
      return byteBuffer;
    }

    public void setByteBuffer(ByteBuffer byteBuffer) {
      this.byteBuffer = byteBuffer;
    }
  }

  final List<Image> images = new ArrayList<Image>();

  public GifAnimation(String path) {
    this.path = path;
  }

  @PostConstruct
  void init() throws IOException {
    Iterator readers = ImageIO.getImageReadersByFormatName("gif");
    ImageReader reader = (ImageReader)readers.next();
    InputStream is = applicationContext.getResource(path).getInputStream();
    assert is != null;
    ImageInputStream iis = ImageIO.createImageInputStream(is);
    assert iis != null;
    reader.setInput(iis, false);

    for (int i = 0; i < reader.getNumImages(true); i++) {
      GifImageIOImageData imageData = new GifImageIOImageData();
      imageData.setByteBuffer(imageData.imageToByteBuffer(reader.read(i), false, false, null));
      images.add(new Image(imageData));
    }
  }

  @Override
  public int getImagesCount() {
    return images.size();
  }

  @Override
  public Image getImage(int i) {
    return images.get(i);
  }

  @Override
  public Image getFlippedImage(int i) {
    // This should be fast enough.
    return images.get(i).getFlippedCopy(true, false);
  }
}
