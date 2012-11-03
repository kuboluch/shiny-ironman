package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.util.Assert;
import kniemkiewicz.jqblocks.util.GeometryUtils;
import kniemkiewicz.jqblocks.util.slick.SimpleGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * User: krzysiek
 * Date: 07.07.12
 */
public class Main {
  
  public static final String contextPath = "context/main.xml";

  // Maybe an enum?
  public static String WINDOW_WIDTH_NAME = "Main.WINDOW_WIDTH";
  public static String WINDOW_HEIGHT_NAME = "Main.WINDOW_HEIGHT";

  public static void main(String[] args) throws SlickException, FileNotFoundException {
    /*
    Circle c1 = new Circle(3, 3, 30);
    Rectangle r = GeometryUtils.getBoundingRectangle(c1);
    SimpleGame.showShapes(Arrays.asList(c1, r));
    */
    ApplicationContext ctx = new ClassPathXmlApplicationContext(contextPath);
    Configuration configuration = ctx.getBean(Configuration.class);
    if (configuration.getBoolean("Main.HIDE_TWL_WHINING", true)) {
      System.setErr(new PrintStream(Assert.noopStream));
    }
    Game game = ctx.getBean(Game.class);
    AppGameContainer app = new AppGameContainer(game);
    int windowWidth = configuration.getInt(WINDOW_WIDTH_NAME, Sizes.DEFAULT_WINDOW_WIDTH);
    int windowHeight = configuration.getInt(WINDOW_HEIGHT_NAME, Sizes.DEFAULT_WINDOW_HEIGHT);
    app.setDisplayMode(windowWidth, windowHeight, false);
    app.start();
  }
}
