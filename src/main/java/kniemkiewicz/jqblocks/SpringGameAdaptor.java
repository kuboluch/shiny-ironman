package kniemkiewicz.jqblocks;

import kniemkiewicz.jqblocks.ingame.controller.EndGameController;
import kniemkiewicz.jqblocks.ingame.Game;
import kniemkiewicz.jqblocks.ingame.controller.SaveGameListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

/**
 * User: krzysiek
 * Date: 07.07.12
 * TODO: StateBasedGame
 */
public class SpringGameAdaptor extends BasicGame implements ApplicationContextAware {

  private static final Log logger = LogFactory.getLog(SpringGameAdaptor.class);

  public static final String gameContextPath = "context/ingame.xml";
  
  BeanFactory gameBeanFactory;
  ApplicationContext mainBeanFactory;
  Game game;
  EndGameController endGameController;

  public SpringGameAdaptor() {
    super("JPioneer");
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {
    // Some beans cannot be initialized before here.
    Game.Settings gameSettings = new Game.Settings();
    gameSettings.seed = 1L;
    initInternal(gameContainer, gameSettings);
  }

  void initInternal(GameContainer gameContainer, Game.Settings gameSettings) throws SlickException {
    gameBeanFactory = null;
    // This a fix for some stupid idea-spring conflict on Linux
    while (gameBeanFactory == null) {
      try{
        gameBeanFactory = new ClassPathXmlApplicationContext(new String[]{gameContextPath}, true, mainBeanFactory);
      } catch (BeanDefinitionStoreException e) {
        logger.error("???", e);
      }
    }
    game = gameBeanFactory.getBean(Game.class);
    game.setSettings(gameSettings);
    endGameController = gameBeanFactory.getBean(EndGameController.class);
    gameContainer.getInput().removeAllListeners();
    System.gc();
    game.init(gameContainer);
  }

  @Override
  public void update(GameContainer gameContainer, int delta) throws SlickException {
    if (endGameController.isGameShouldRestart()) {
      Game.Settings settings = new Game.Settings();
      try {
        FileInputStream input = new FileInputStream(SaveGameListener.FILENAME);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        settings.savegame = objectInputStream;
        initInternal(gameContainer, settings);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (endGameController.isGameShouldEnd()) {
      gameContainer.exit();
    }
    game.update(gameContainer, delta);
  }

  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    game.render(gameContainer, graphics);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.mainBeanFactory = applicationContext;
  }
}
