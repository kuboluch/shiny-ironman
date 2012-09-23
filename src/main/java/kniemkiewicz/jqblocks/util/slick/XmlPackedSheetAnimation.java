package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.Image;
import org.newdawn.slick.XMLPackedSheet;

import java.util.List;

/**
 * User: qba
 * Date: 22.09.12
 */
public class XmlPackedSheetAnimation implements Animation {

  final XMLPackedSheet spriteSheet;
  final List<String> spriteSequence;

  public XmlPackedSheetAnimation(XMLPackedSheet spriteSheet, List<String> spriteSequence) {
    this.spriteSheet = spriteSheet;
    this.spriteSequence = spriteSequence;
  }

  @Override
  public int getImagesCount() {
    return spriteSequence.size();
  }

  @Override
  public Image getImage(int i) {
    return spriteSheet.getSprite(spriteSequence.get(i));
  }

  @Override
  public Image getFlippedImage(int i) {
    return spriteSheet.getSprite(spriteSequence.get(i)).getFlippedCopy(true, false);
  }
}
