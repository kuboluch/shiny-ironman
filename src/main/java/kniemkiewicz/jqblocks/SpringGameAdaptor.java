package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.twl.TWLStateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;

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
    return SpringGameAdaptor.class.getResource("/ui/default/simple.xml");
  }
}
