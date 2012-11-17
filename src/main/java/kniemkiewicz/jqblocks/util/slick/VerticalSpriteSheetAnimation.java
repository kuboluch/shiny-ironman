package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * User: qba
 * Date: 08.10.12
 */
public class VerticalSpriteSheetAnimation implements Animation {

  final SpriteSheet spritesheet;

  public VerticalSpriteSheetAnimation(SpriteSheet spritesheet) {
    this.spritesheet = spritesheet;
  }

  @Override
  public int getImagesCount() {
    return spritesheet.getVerticalCount();
  }

  @Override
  public Image getImage(int i) {
    if (i >= getImagesCount()) return spritesheet.getSprite(0, getImagesCount() - 1);
    return spritesheet.getSprite(0, i);
  }

  @Override
  public Image getFlippedImage(int i) {
    return spritesheet.getSprite(0, i).getFlippedCopy(true, false);
  }
}