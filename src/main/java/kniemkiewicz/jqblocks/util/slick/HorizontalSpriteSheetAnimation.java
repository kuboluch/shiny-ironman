package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * User: krzysiek
 * Date: 14.09.12
 */
public class HorizontalSpriteSheetAnimation implements Animation {

  final SpriteSheet spritesheet;

  public HorizontalSpriteSheetAnimation(SpriteSheet spritesheet) {
    this.spritesheet = spritesheet;
  }

  @Override
  public int getImagesCount() {
    return spritesheet.getHorizontalCount();
  }

  @Override
  public Image getImage(int i) {
    return spritesheet.getSprite(i, 0);
  }

  @Override
  public Image getFlippedImage(int i) {
    return spritesheet.getSprite(i, 0).getFlippedCopy(true, false);
  }
}
