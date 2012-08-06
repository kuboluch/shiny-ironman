package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.ingame.Sizes;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: krzysiek
 * Date: 07.07.12
 */
public class Main {
  
  public static final String contextPath = "context/main.xml";

  // Maybe an enum?
  public static String WINDOW_WIDTH_NAME = "Main.WINDOW_WIDTH";
  public static String WINDOW_HEIGHT_NAME = "Main.WINDOW_HEIGHT";

  public static void main(String[] args) throws SlickException {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(contextPath);
    Configuration configuration = ctx.getBean(Configuration.class);
    Game game = ctx.getBean(Game.class);
    AppGameContainer app = new AppGameContainer(game);
    int windowWidth = configuration.getInt(WINDOW_WIDTH_NAME, Sizes.DEFAULT_WINDOW_WIDTH);
    int windowHeight = configuration.getInt(WINDOW_HEIGHT_NAME, Sizes.DEFAULT_WINDOW_HEIGHT);
    app.setDisplayMode(windowWidth, windowHeight, false);
    app.start();
  }
}
