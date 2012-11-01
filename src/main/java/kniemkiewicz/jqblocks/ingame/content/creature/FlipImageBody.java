package kniemkiewicz.jqblocks.ingame.content.creature;

import kniemkiewicz.jqblocks.ingame.controller.AgeUpdateController;
import kniemkiewicz.jqblocks.ingame.controller.FreeFallController;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.ImageRenderer;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.newdawn.slick.geom.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: knie
 * Date: 10/27/12
 */
public class FlipImageBody extends SimpleBody implements UpdateQueue.ToBeUpdated<AgeUpdateController.HasAge>, AgeUpdateController.HasAge{

  final BeanName<? extends ImageRenderer> liveRenderer;
  long age = 0;
  transient Vector2f imageCenterShift;

  public static <T extends HasFullXYMovement & RenderableObject> FlipImageBody newInstance(T object, Vector2f imageCenterShift) {
    return new FlipImageBody(object.getXYMovement(), object.getShape().getWidth(), object.getShape().getHeight(), object.getRenderer(), imageCenterShift);
  }

  public FlipImageBody(XYMovement movement, float width, float height, BeanName<? extends ImageRenderer> liveRenderer, Vector2f imageCenterShift) {
    super(movement, width, height);
    this.liveRenderer = liveRenderer;
    this.imageCenterShift = imageCenterShift;
  }

  private static final BeanName<FlipImageRenderer> RENDERER = new BeanName<FlipImageRenderer>(FlipImageRenderer.class);

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return RENDERER;
  }

  public BeanName<? extends ImageRenderer> getLiveRenderer() {
    return liveRenderer;
  }

  @Override
  public Class<? extends UpdateQueue.UpdateController<? super AgeUpdateController.HasAge>> getUpdateController() {
    return AgeUpdateController.class;
  }

  public boolean addTo(RenderQueue renderQueue, FreeFallController freeFallController, UpdateQueue updateQueue) {
    updateQueue.add(this);
    return super.addTo(renderQueue, freeFallController);
  }

  @Override
  public long getAge() {
    return age;
  }

  @Override
  public void setAge(long age) {
    this.age = age;
  }

  public Vector2f getImageCenterShift() {
    return imageCenterShift;
  }

  // need to implement serialization as Vector2f is not Serializable
  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    imageCenterShift = SerializationUtils2.deserializeVector2f(inputStream);
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    //perform the default serialization for all non-transient, non-static fields
    outputStream.defaultWriteObject();
    SerializationUtils2.serializeVector2f(imageCenterShift, outputStream);
  }
}
