package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.ingame.MainGameState;
import kniemkiewicz.jqblocks.twl.TWLStateBasedGame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;

/**
 * User: krzysiek
 * Date: 07.07.12
 */
public class SpringGameAdaptor extends TWLStateBasedGame {

  @Autowired
  SpringGameStateAdaptor mainGameState;

  public SpringGameAdaptor() {
    super("JPioneer");
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    addState(mainGameState);
  }

  @Override
  protected URL getThemeURL() {
    return SpringGameAdaptor.class.getResource("/ui/theme.xml");
  }
}
