package kniemkiewicz.jqblocks.ingame.content.item.arrow;

import kniemkiewicz.jqblocks.ingame.HasFullXYMovement;
import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.ingame.UpdateQueue;
import kniemkiewicz.jqblocks.ingame.content.item.bow.BowRenderer;
import kniemkiewicz.jqblocks.ingame.object.HasSource;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.ingame.util.QuadTree;
import kniemkiewicz.jqblocks.ingame.util.movement.MovementDefinition;
import kniemkiewicz.jqblocks.ingame.util.movement.SingleAxisMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovement;
import kniemkiewicz.jqblocks.ingame.util.movement.XYMovementDefinition;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: knie
 * Date: 7/21/12
 */
public class Arrow implements RenderableObject<Arrow>,UpdateQueue.ToBeUpdated<Arrow>,HasSource {

  private static final long serialVersionUID = 1;
  private static XYMovementDefinition ARROW_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition(),new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  transient Line line;
  XYMovement movement;
  //TODO: fix this, we need global object ids or something like that.
  transient QuadTree.HasShape source;
  private static int LENGTH = Sizes.BLOCK;

  public Arrow(float x, float y, QuadTree.HasShape source, float xSpeed, float ySpeed) {
    this.line = new Line(x, y, x, y);
    this.movement = ARROW_MOVEMENT.getMovement(x, y).setXSpeed(xSpeed).setYSpeed(ySpeed);
    this.source = source;
  }

  @Override
  public BeanName<? extends ObjectRenderer<Arrow>> getRenderer() {
    return null;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setColor(BowRenderer.ARROW_COLOR);
    g.setLineWidth(2);
    g.draw(line);
    g.setLineWidth(1);
  }

  @Override
  public Shape getShape() {
    return line;
  }

  @Override
  public Layer getLayer() {
    return Layer.ARROWS;
  }


  public void update(int delta) {
    SingleAxisMovement xMovement = movement.getXMovement();
    SingleAxisMovement yMovement = movement.getYMovement();
    yMovement.setAcceleration(Sizes.G);
    xMovement.update(delta);
    yMovement.update(delta);
    float dx = xMovement.getSpeed();
    float dy = yMovement.getSpeed();
    float v = (float)Math.sqrt(dx * dx + dy * dy);
    assert v > 0;
    float lx = dx / v * LENGTH / 2;
    float ly = dy / v * LENGTH / 2;
    line.set(xMovement.getPos() - lx, yMovement.getPos() - ly, xMovement.getPos() + lx, yMovement.getPos() + ly);
  }

  public XYMovement getXMovement() {
    return movement;
  }

  public QuadTree.HasShape getSource() {
    return source;
  }

  @Override
  public Class<ArrowController> getUpdateController() {
    return ArrowController.class;
  }

  // need to implement serialization as Circle is not Serializable
  private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    //always perform the default de-serialization first
    inputStream.defaultReadObject();
    line = SerializationUtils2.deserializeLine(inputStream);
  }

  private void writeObject(ObjectOutputStream outputStream) throws IOException {
    //perform the default serialization for all non-transient, non-static fields
    outputStream.defaultWriteObject();
    SerializationUtils2.serializeLine(line, outputStream);
  }
}
