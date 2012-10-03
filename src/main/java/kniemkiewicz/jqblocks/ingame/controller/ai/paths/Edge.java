package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import kniemkiewicz.jqblocks.ingame.PointOfView;
import kniemkiewicz.jqblocks.ingame.object.ObjectRenderer;
import kniemkiewicz.jqblocks.ingame.object.RenderableObject;
import kniemkiewicz.jqblocks.util.BeanName;
import kniemkiewicz.jqblocks.util.SerializationUtils2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: krzysiek
 * Date: 26.09.12
 */
public class Edge implements RenderableObject<Edge>{

  enum Type {
    FLAT,
    HORIZONTAL_LADDER,
    VERTICAL_LADDER
  }
  transient Line line;
  Type type;

  List<Joint> joints = new ArrayList<Joint>();

  Edge(Line line, Type type) {
    this.line = line;
    this.type = type;
  }

  @Override
  public BeanName<? extends ObjectRenderer> getRenderer() {
    return null;
  }

  final float getXFor(float pos) {
    return line.getX1() + line.getDX() * pos;
  }

  final float getYFor(float pos) {
    return line.getY1() + line.getDY() * pos;
  }

  @Override
  public void renderObject(Graphics g, PointOfView pov) {
    g.setLineWidth(2);
    g.setColor(Color.red);
    g.draw(line);
    if (joints.size() > 0) {
      g.setColor(Color.orange);
      Rectangle r = new Rectangle(0,0,4,4);
      for (Joint j : joints) {
        r.setCenterX(getXFor(j.getPosition()));
        r.setCenterY(getYFor(j.getPosition()));
      }
      g.draw(r);
    }
    g.resetLineWidth();
  }

  Joint addJoint(float pos, Edge e) {
    assert e != this;
    Joint j = new Joint(pos, e);
    joints.add(j);
    return j;
  }

  Joint addJoint(Vector2f intersection, Edge e) {
    if (line.getDX() > line.getDY()) {
      return addJoint((intersection.getX() - line.getX()) / line.getDX(), e);
    } else {
      return addJoint((intersection.getY() - line.getY()) / line.getDY(), e);
    }
  }

  @Override
  public Layer getLayer() {
    return Layer.PLUS_INF;
  }

  @Override
  public Shape getShape() {
    return line;
  }

  public boolean joins(Edge edge) {
    for (Joint j : joints) {
      if (j.getEdge() == edge) return true;
    }
    return false;
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
