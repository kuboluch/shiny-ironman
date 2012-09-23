package kniemkiewicz.jqblocks.ingame.content.item.arrow;

import kniemkiewicz.jqblocks.ingame.*;
import kniemkiewicz.jqblocks.ingame.object.hp.KillablePhysicalObject;
import kniemkiewicz.jqblocks.ingame.content.item.bow.BowRenderer;
import kniemkiewicz.jqblocks.ingame.controller.ProjectileController;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.serialization.SerializableRef;
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
public class Arrow implements ProjectileController.Projectile<Arrow> {

  private int ARROW_DMG = 10;

  private static final long serialVersionUID = 1;
  private static XYMovementDefinition ARROW_MOVEMENT = new XYMovementDefinition(
      new MovementDefinition(),new MovementDefinition().setMaxSpeed(Sizes.MAX_FALL_SPEED)
  );

  transient Line line;
  final XYMovement movement;
  final private SerializableRef<QuadTree.HasShape> source;
  private static int LENGTH = Sizes.BLOCK;

  public Arrow(float x, float y, QuadTree.HasShape source, float xSpeed, float ySpeed) {
    this.line = new Line(x, y, x, y);
    this.movement = ARROW_MOVEMENT.getMovement(x, y).setXSpeed(xSpeed).setYSpeed(ySpeed);
    this.source = new SerializableRef<QuadTree.HasShape>(source);
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  static void renderArrow(Graphics g, Line line) {
    g.setColor(BowRenderer.ARROW_COLOR);
    g.setLineWidth(2);
    g.draw(line);
    g.setLineWidth(1);
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    renderArrow(g, line);

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

  @Override
  public boolean hitWall(World world) {
    return true;
  }

  public XYMovement getXMovement() {
    return movement;
  }

  public QuadTree.HasShape getSource() {
    return source.get();
  }

  @Override
  public Class<ProjectileController> getUpdateController() {
    return ProjectileController.class;
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


  public void hitTarget(KillablePhysicalObject kpo, World world) {
    kpo.getHp().damage(ARROW_DMG, this, world);
    // This makes stuck arrow appear much deeper in target.
    this.update(10);
    StuckArrow stuckArrow = new StuckArrow(line, kpo);
    stuckArrow.addTo(world.getRenderQueue(), world.getUpdateQueue());
  }
}
