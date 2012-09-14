package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.ZombieBody;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.util.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 11.09.12
 */
public class AnimationRenderer implements ObjectRenderer<ZombieBody> {

  final Animation animation;

  float width;
  float shiftTop = 0;
  float shiftHeight = 0;
  int frameDuration = 125;
  boolean repeated = true;

  public AnimationRenderer(Animation animation) {
    this.animation = animation;
    this.width = animation.getImage(0).getWidth();
  }

  public void setShiftTop(float shiftTop) {
    this.shiftTop = shiftTop;
  }

  public void setShiftHeight(float shiftHeight) {
    this.shiftHeight = shiftHeight;
  }


  public void setWidth(float width) {
    this.width = width;
  }

  public void setFrameDuration(int frameDuration) {
    this.frameDuration = frameDuration;
  }

  public void setRepeated(boolean repeated) {
    this.repeated = repeated;
  }

  @Override
  public void render(ZombieBody object, Graphics g, PointOfView pov) {
    float objectWidth = object.getShape().getWidth();
    float widthDiff = (width - objectWidth) / 2;
    int spriteId;
    if (repeated) {
      spriteId = (object.getAge() / frameDuration)  % animation.getImagesCount();
    } else {
      spriteId = Math.min(object.getAge() / frameDuration, animation.getImagesCount() - 1);
    }
    Rectangle r = object.getShape();
    Image sprite;
    if (object.getXYMovement().getXMovement().getDirection()) {
      sprite = animation.getFlippedImage(spriteId);
    } else {
      sprite = animation.getImage(spriteId);
    }
    sprite.draw(r.getX() - widthDiff, r.getY() + shiftTop, width, r.getHeight() + shiftHeight);
  }
}
