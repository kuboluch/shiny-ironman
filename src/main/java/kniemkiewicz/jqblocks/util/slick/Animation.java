package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.Image;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
public interface Animation {
  int getImagesCount();

  Image getImage(int i);

  Image getFlippedImage(int i);
}
