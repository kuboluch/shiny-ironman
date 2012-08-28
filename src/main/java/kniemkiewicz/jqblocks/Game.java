package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.twl.RootPane;
import kniemkiewicz.jqblocks.twl.TWLStateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

/**
 * User: krzysiek
 * Date: 07.07.12
 */
public class Game extends TWLStateBasedGame {

  @Autowired
  SpringMainGameStateAdaptor gameState;

  public Game() {
    super("JPioneer");
  }

  @Override
  public void initStatesList(GameContainer gameContainer) throws SlickException {
    addState(gameState);
  }

  @Override
  protected URL getThemeURL() {
    return Game.class.getResource("/ui/default/simple.xml");
  }

  public void changeRootPane(RootPane rootPane) throws SlickException {
    super.setRootPane(rootPane);
  }
}
