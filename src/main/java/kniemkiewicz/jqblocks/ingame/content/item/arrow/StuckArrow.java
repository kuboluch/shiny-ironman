package kniemkiewicz.jqblocks.ingame.content.item.arrow;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.controller.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.object.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.renderer.RenderQueue;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: knie
 * Date: 7/21/12
 */
public class StuckArrow implements RenderableObject<StuckArrow>,UpdateQueue.ToBeUpdated<StuckArrow> {

  private static final long serialVersionUID = 1;

  transient Line line;
  transient Vector2f diff;
  final KillablePhysicalObject target;
  Boolean lastDirection = null;

  public StuckArrow(Line line, KillablePhysicalObject target) {
    this.line = line;
    this.target = target;
    this.diff = new Vector2f(line.getCenterX() - target.getShape().getCenterX(), line.getCenterY() - target.getShape().getCenterY());
    if (target instanceof HasFullXYMovement) {
      this.lastDirection = ((HasFullXYMovement) target).getXYMovement().getXMovement().getDirection();
    }
  }

  public void addTo(RenderQueue renderQueue, UpdateQueue updateQueue) {
    renderQueue.add(this);
    updateQueue.add(this);
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    Arrow.renderArrow(g, line);
  }

  @Override
  public Shape getShape() {
    return line;
  }

  @Override
  public Layer getLayer() {
    return Layer.ARROWS;
  }


  public void update() {
    if (lastDirection != null) {
      if (this.lastDirection != ((HasFullXYMovement) target).getXYMovement().getXMovement().getDirection()) {
        this.lastDirection = ((HasFullXYMovement) target).getXYMovement().getXMovement().getDirection();
        // flip line horizontally
        line.set(line.getX2(), line.getY1(), line.getX1(), line.getY2());
        diff.set(- diff.getX(), diff.getY());
      }
    }
    line.setCenterX(this.target.getShape().getCenterX() + diff.getX());
    line.setCenterY(this.target.getShape().getCenterY() + diff.getY());
  }

  KillablePhysicalObject getTarget(){
    return target;
  }

  @Override
  public Class<StuckArrowController> getUpdateController() {
    return StuckArrowController.class;
  }

  // need to implement serialization as Circle is not Serializable
  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    line = SerializationUtils2.deserializeLine(inputStream);
    diff = SerializationUtils2.deserializeVector2f(inputStream);
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    //perform the default serialization for all non-transient, non-static fields
    outputStream.defaultWriteObject();
    SerializationUtils2.serializeLine(line, outputStream);
    SerializationUtils2.serializeVector2f(diff, outputStream);
  }
}
