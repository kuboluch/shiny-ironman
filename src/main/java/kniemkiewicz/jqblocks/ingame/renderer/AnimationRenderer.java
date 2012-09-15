package kniemkiewicz.jqblocks.ingame.renderer;

import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.content.creature.zombie.ZombieBody;
import kniemkiewicz.jqblocks.ingame.content.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * User: krzysiek
 * Date: 11.09.12
 */
public class AnimationRenderer<T extends AnimationRenderer.AnimationCompatible> implements ObjectRenderer<T> {

  public interface AnimationCompatible<T extends RenderableObject> extends HasFullXYMovement, QuadTree.HasShape, RenderableObject<T>{
    int getAge();
  }

  final Animation animation;

  float width;
  float shiftTop = 0;
  float shiftHeight = 0;
  int frameDuration = 125;
  boolean repeated = true;
  boolean debug = false;

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

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  @Override
  public void render(T object, Graphics g, PointOfView pov) {
    float objectWidth = object.getShape().getWidth();
    float widthDiff = (width - objectWidth) / 2;
    int spriteId;
    if (repeated) {
      spriteId = (object.getAge() / frameDuration)  % animation.getImagesCount();
    } else {
      spriteId = Math.min(object.getAge() / frameDuration, animation.getImagesCount() - 1);
    }
    Rectangle r = GeometryUtils.getBoundingRectangle(object.getShape());
    Image sprite;
    if (object.getXYMovement().getXMovement().getDirection()) {
      sprite = animation.getFlippedImage(spriteId);
    } else {
      sprite = animation.getImage(spriteId);
    }
    sprite.draw(r.getX() - widthDiff, r.getY() + shiftTop, width, r.getHeight() + shiftHeight);
    if (debug) {
      g.setColor(Color.red);
      g.draw(r);
    }
  }
}
