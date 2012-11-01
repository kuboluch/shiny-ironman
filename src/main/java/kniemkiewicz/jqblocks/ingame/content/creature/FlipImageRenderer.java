package kniemkiewicz.jqblocks.ingame.content.creature;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.util.SpringBeanProvider;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: knie
 * Date: 10/27/12
 */
@Component
public class FlipImageRenderer implements ObjectRenderer<FlipImageBody> {

  @Autowired
  SpringBeanProvider springBeanProvider;

  static float ANGULAR_SPEED = 0.7f;

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
    g.translate(- cx, - cy);
    imageRenderer.render(object, g, pov);
    g.popTransform();
  }
}
