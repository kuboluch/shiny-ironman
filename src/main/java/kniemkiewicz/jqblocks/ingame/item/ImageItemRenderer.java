package kniemkiewicz.jqblocks.ingame.item;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * User: knie
 * Date: 7/29/12
 */
// This class has to be explicitly defined in xml per each item, to allow
// setting different images.
public class ImageItemRenderer implements ItemRenderer<Item> {

  Image image;

  public ImageItemRenderer(Image image) {
    this.image = image;
  }

  @Override
  public void renderItem(Item item, Graphics g, int x, int y, int square_size) {
    image.draw(x,y,square_size, square_size);
  }
}
