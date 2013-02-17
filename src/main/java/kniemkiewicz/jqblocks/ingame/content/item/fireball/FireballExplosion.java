package kniemkiewicz.jqblocks.ingame.content.item.fireball;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.AnimationRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * User: krzysiek
 * Date: 24.09.12
 */
public class FireballExplosion implements AnimationRenderer.AnimationCompatible, UpdateQueue.ToBeUpdated<FireballExplosion>{

  Rectangle rectangle;
  static public int SIZE = Sizes.BLOCK * 2;
  static public int MAX_AGE = 10 * 100; // 10 frames, 100ms each.

  int age;


  public FireballExplosion(float centerX, float centerY) {
    rectangle = new Rectangle(0,0, SIZE, SIZE);
    rectangle.setCenterX(centerX);
    rectangle.setCenterY(centerY);
  }

  void addTo(RenderQueue renderQueue, UpdateQueue updateQueue) {
    renderQueue.add(this);
    updateQueue.add(this);
  }

  @Override
  public long getAge() {
    return age;
  }

  @Override
  public XYMovement getXYMovement() {
    return null;
  }

  @Override
  public void updateShape() {  }

  private BeanName<AnimationRenderer> RENDERER = new BeanName<AnimationRenderer>(AnimationRenderer.class, "fireballExplosionRenderer");

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) { }

  @Override
  public Layer getLayer() {
    return Layer.PLUS_INF;
  }

  @Override
  public Shape getShape() {
    return rectangle;
  }

  @Override
  public Class<FireballExplosionController> getUpdateController() {
    return FireballExplosionController.class;
  }
}
