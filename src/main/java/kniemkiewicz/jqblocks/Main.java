package kniemkiewicz.jqblocks;

import de.matthiasmann.twl.DebugHook;
import kniemkiewicz.jqblocks.ingame.Sizes;
import kniemkiewicz.jqblocks.twl.SilentDebugHook;
import kniemkiewicz.jqblocks.util.Assert;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.PrintStream;

/**
 * User: krzysiek
 * Date: 07.07.12
 */
public class Main {
  
  public static final String contextPath = "context/main.xml";

  // Maybe an enum?
  public static String WINDOW_WIDTH_NAME = "Main.WINDOW_WIDTH";
  public static String WINDOW_HEIGHT_NAME = "Main.WINDOW_HEIGHT";

  static PrintStream STD_ERR = null;

  public static void main(String[] args) throws SlickException {
    /*
    Circle c1 = new Circle(3, 3, 30);
    Rectangle r = GeometryUtils.getBoundingRectangle(c1);
    SimpleGame.showShapes(Arrays.asList(c1, r));
    */
    ApplicationContext ctx = new ClassPathXmlApplicationContext(contextPath);
    Configuration configuration = ctx.getBean(Configuration.class);
    configuration.initArgs(args);
    STD_ERR = System.err;
    if (configuration.getBoolean("Main.HIDE_TWL_WHINING", true)) {
      DebugHook.installHook(new SilentDebugHook());
    }
    Game game = ctx.getBean(Game.class);
    AppGameContainer app = new AppGameContainer(game);
    int windowWidth = configuration.getInt(WINDOW_WIDTH_NAME, Sizes.DEFAULT_WINDOW_WIDTH);
    int windowHeight = configuration.getInt(WINDOW_HEIGHT_NAME, Sizes.DEFAULT_WINDOW_HEIGHT);
    app.setDisplayMode(windowWidth, windowHeight, false);
    app.start();
  }
}
