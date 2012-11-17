package kniemkiewicz.jqblocks.ingame.content.creature;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import kniemkiewicz.jqblocks.util.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: knie
 * Date: 10/27/12
 */
@Component
public class FlipImageRenderer implements ObjectRenderer<FlipImageBody> {

  @Autowired
  SpringBeanProvider springBeanProvider;

  @Resource
  Animation bloodPoolAnimation;

  static float ANGULAR_SPEED = 0.7f;
  static float START_FADING_TIME = 180 / ANGULAR_SPEED;
  static float FADING_DURATION = 630;
  static float BLOOD_FRAME_LENGTH = 2 * FADING_DURATION / 5;

  @Override
  public void render(FlipImageBody object, Graphics g, PointOfView pov) {
    ImageRenderer imageRenderer = springBeanProvider.getBean(object.getLiveRenderer(), true);
    g.pushTransform();
    Vector2f imageCenterShift = object.getImageCenterShift();
    float cx = object.getShape().getCenterX() + imageCenterShift.getX();
    float cy = object.getShape().getCenterY() + imageCenterShift.getY();
    g.translate(cx, cy);
    if (object.getXYMovement().getXMovement().getDirection()) {
      g.rotate(0,0, Math.min(object.getAge() * ANGULAR_SPEED, 180));
    } else {
      g.rotate(0,0, Math.max(- object.getAge() * ANGULAR_SPEED, - 180));
    }
    Shape shape = object.getShape();
    if (object.getAge() > START_FADING_TIME) {
      float gr = 1 - 0.25f * Math.min(1f, (object.getAge() - START_FADING_TIME * 1f) / FADING_DURATION);
      Color color = new Color(gr, gr, gr);
      imageRenderer.getImage().draw(-shape.getWidth() / 2, -shape.getHeight() / 2, shape.getWidth(), shape.getHeight(), color);
    } else {
      imageRenderer.getImage().draw(-shape.getWidth() / 2, -shape.getHeight() / 2, shape.getWidth(), shape.getHeight());
    }
    g.popTransform();

    if (object.getAge() > START_FADING_TIME) {
      // Skip first frame.
      Image im = bloodPoolAnimation.getImage((int)((object.getAge() - START_FADING_TIME) / BLOOD_FRAME_LENGTH + 1));
      float scale = 3 * object.getShape().getWidth() / im.getWidth();
      float w = im.getWidth() * scale;
      float h = 2 * im.getHeight() * scale;
      im.draw(object.getShape().getCenterX() - w / 2, object.getShape().getMaxY() - h * 3 / 4, w, h);
    }
  }
}
