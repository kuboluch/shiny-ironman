package kniemkiewicz.jqblocks.ingame.controller.ai.paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: krzysiek
 * Date: 03.11.12
 */
public class PathGraphSearchTest {

  public static Log logger = LogFactory.getLog(PathGraphSearchTest.class);

  /**
   * First we define few points:
   */
  Vector2f A = new Vector2f(0, 0);
  Vector2f B = new Vector2f(10, 0);
  Vector2f C = new Vector2f(10, 10);
  Vector2f D = new Vector2f(1, 1);
  Vector2f E = new Vector2f(3, 3);
  Vector2f F = new Vector2f(3, 3);
  Vector2f G = new Vector2f(40, 0);
  Vector2f H = new Vector2f(10, -1);
  Vector2f I = new Vector2f(40, 20);
  /**
   * Edges always join some of point above and are named after them.
   */
  Edge abg = new Edge(new Line(A, G), Edge.Type.TEST, "abg");
  Edge bc = new Edge(new Line(B, C), Edge.Type.TEST, "bc");
  Edge ad = new Edge(new Line(A, D), Edge.Type.TEST, "ad");
  Edge de = new Edge(new Line(D, E), Edge.Type.TEST, "de");
  Edge ec = new Edge(new Line(E, C), Edge.Type.TEST, "ec");
  Edge cf = new Edge(new Line(C, F), Edge.Type.TEST, "cf");
  Edge bh = new Edge(new Line(B, H), Edge.Type.TEST, "bh");
  Edge gi = new Edge(new Line(G, I), Edge.Type.TEST, "gi");
  Edge hi = new Edge(new Line(H, I), Edge.Type.TEST, "hi");

  void join(Edge a, Edge b, float posA, float posB) {
    a.addJoint(posA, b).with(b.addJoint(posB, a));
  }

  public PathGraphSearchTest() {
    // A
    join(abg, ad, 0, 0);
    // D
    join(ad, de, 1 , 0);
    // E
    join(de, ec, 1 , 0);
    // B
    join(bc, abg, 0, 0.25f);
    join(bh, bc, 0, 0);
    join(bh, abg, 0, 0.25f);
    // C
    join(ec, bc, 1, 1);
    join(bc, cf, 1, 0);
    join(ec, cf, 1, 0);
    // G
    join(abg, gi, 1, 0);
    // H
    join(bh, hi, 1, 0);
    // I
    join(hi, gi, 1, 1);
  }

  Edge[] getEdgesBetween(Position p1, Position p2) {
    Path path = new PathGraphSearch(p1, p2).getPath();
    List<Position> positions = path.computePositions();
    List<Edge> edges = new ArrayList<Edge>();
    for (Position p : positions) {
      edges.add(p.getEdge());
    }
    logger.info(edges);
    return edges.toArray(new Edge[edges.size()]);
  }

  @Test
  public void testA2F() {
    Position a = new Position(abg, 0);
    Position f = new Position(cf, 1);
    Assert.assertArrayEquals(getEdgesBetween(a, f), new Edge[]{abg, abg, ad, de, ec, cf});
  }

  @Test
  public void testAB2F() {
    Position ab = new Position(abg, 0.25f);
    Position f = new Position(cf, 1);
    Assert.assertArrayEquals(getEdgesBetween(ab, f), new Edge[]{abg, abg, bc, cf});
  }

  @Test
  public void testGE() {
    Position g = new Position(abg, 1);
    Position e = new Position(ec, 0);
    Assert.assertArrayEquals(getEdgesBetween(g, e), new Edge[]{abg, abg, ad, de, ec});
  }

  @Test
  public void testIF() {
    Position i = new Position(gi, 1);
    Position f = new Position(cf, 1);
    Assert.assertArrayEquals(getEdgesBetween(i, f), new Edge[] {gi, gi, hi, bh, bc, cf});
  }

  @Test
  public void testAG() {
    Position a = new Position(abg, 0);
    Position g = new Position(abg, 1);
    Assert.assertArrayEquals(getEdgesBetween(a, g), new Edge[] {abg, abg});
    a = new Position(ad, 0);
    g = new Position(gi, 0);
    Assert.assertArrayEquals(getEdgesBetween(a, g), new Edge[] {ad, ad, abg, gi});
  }
}
