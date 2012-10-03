package kniemkiewicz.jqblocks.util.slick;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Shape;

import java.util.List;

/**
 * User: krzysiek
 * Date: 01.10.12
 */
public class SimpleGame implements Game {

  interface RenderMethod {
    void render(Graphics g);
  }

  public static void showShapes(final List<Shape> shapes) {
    start(new RenderMethod() {
      @Override
      public void render(Graphics g) {
        for (Shape s : shapes) {
          g.draw(s);
        }
      }
    });
  }

  public static void start(RenderMethod m) {
    SimpleGame game = new SimpleGame(m);
    try {
      AppGameContainer app = new AppGameContainer(game);
      app.setDisplayMode(800, 600, false);
      app.start();
    } catch (SlickException e) {
      throw new RuntimeException("", e);
    }
  }

  final RenderMethod renderMethod;

  public SimpleGame(RenderMethod renderMethod) {
    this.renderMethod = renderMethod;
  }

  @Override
  public void init(GameContainer container) throws SlickException {  }

  @Override
  public void update(GameContainer container, int delta) throws SlickException {

  }

  @Override
  public void render(GameContainer container, Graphics g) throws SlickException {
    g.setBackground(Color.white);
    g.translate(400, 300);
    g.setColor(Color.black);
    renderMethod.render(g);
  }

  @Override
  public boolean closeRequested() {
    return false;
  }

  @Override
  public String getTitle() {
    return "test";
  }
}
