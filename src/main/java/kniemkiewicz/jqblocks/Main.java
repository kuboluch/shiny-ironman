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
  
  public static void main(String[] args) throws SlickException {
    ApplicationContext ctx = new ClassPathXmlApplicationContext(contextPath);
    SpringGameAdaptor springGameAdaptor = ctx.getBean(SpringGameAdaptor.class);
    AppGameContainer app = new AppGameContainer(springGameAdaptor);

    app.setDisplayMode(Sizes.WINDOW_WIDTH, Sizes.WINDOW_HEIGHT, false);
    app.start();
  }
}
